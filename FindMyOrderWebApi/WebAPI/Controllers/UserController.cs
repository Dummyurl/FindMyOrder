using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using WebAPI.Models;
using System.Net.Mail;
using System.Net;
using Microsoft.EntityFrameworkCore;

using DnsClient;
using MailKit.Net.Smtp;
using MailKit;
using MimeKit;
using Microsoft.AspNetCore.Authorization;
using WebAPI.Hashing;
using System.Security.Cryptography;

namespace WebAPI.Controllers
{
    //[Route("api/User")]
    [AllowAnonymous]
    public class UserController : Controller
    {
        private readonly MyDbContext _context;

        public UserController(MyDbContext context)
        {
            _context = context;
        }

        [HttpGet]
        [Route("api/User/GetAll")]
        public Dictionary<string, List<User>> GetAll()
        {
            var Users = _context.User.Include(x => x.UserDetails).ToList();



            //foreach (var user in Users)
            //{    
            //    user.Password = PassCrypt.EnryptString("test123");
            //    _context.User.Update(user);
            //}
            //_context.SaveChanges();

            foreach (var usr in Users)
            {
                if (usr.UserDetails != null)
                {
                    usr.UserDetails.User = null;
                }
                usr.ClientUserRel = null;
                usr.CourierUserRel = null;
                usr.ChatMessages = null;
                usr.CourierLocation = null;
                usr.ContactMessages = null;
                usr.ReportProblemMessages = null;
                usr.DocImages = null;
            }


            Dictionary<string, List<User>> myDictionary = new Dictionary<string, List<User>>();

            myDictionary.Add("Users", Users);

            return myDictionary;
        }

        [HttpGet]
        [Route("api/User/GetById/{id}")]
        public User GetById(long id)
        {
            var item = _context.User.FirstOrDefault(t => t.Id == id);

            return item;
        }

        [HttpGet]
        [Route("api/User/LoginClient/{email}/{password}")]
        public User LoginClient (string email, string password)
        {
            string cryptPass = PassCrypt.EnryptString(password);

            var item = _context.User.Where(t => t.Email == email && t.Password == cryptPass && t.IsCurier == false).Include(x => x.UserDetails).FirstOrDefault();

            if(item != null)
            {
                item.Password = PassCrypt.DecryptString(cryptPass);
                item.UserDetails.User = null;
            }
            return item;

        }

        [HttpGet]
        [Route("api/User/LoginCourier/{email}/{password}")]
        public User LoginCourier(string email, string password)
        {

            string cryptPass = PassCrypt.EnryptString(password);

            var item = _context.User.Where(t => t.Email == email && t.Password == cryptPass && t.IsCurier == true).Include(x => x.UserDetails).FirstOrDefault();

            if (item != null)
            {
                item.Password = PassCrypt.DecryptString(cryptPass);
                item.UserDetails.User = null;
            }
            return item;

        }

        [HttpGet]
        [Route("api/User/ResetPass/{email}")]
        public User ResetPass(string email)
        {

            //check if email in database
            var Users = _context.User.ToList();
            var isInDatabase = false;
            foreach (var us in Users)
            {
                if (us.Email == email)
                {
                    isInDatabase = true;
                }
            }

            if (isInDatabase == false)
            {
                return new User();
            }
            else
            {
                var account = _context.User.FirstOrDefault(t => t.Email == email);

                

                //decriptam parola veche
                account.OldPassword = PassCrypt.DecryptString(account.Password);
                string ResetCode = Guid.NewGuid().ToString("N").Substring(0, 10);

                //schimbam parola veche cu una noua criptata
                account.Password = PassCrypt.EnryptString(ResetCode);
                _context.User.Update(account);
                _context.SaveChanges();

                int x = SendResetPasswordEmail(email, ResetCode);

                account.UserDetails = null;

                if(account.UserDetails != null)
                {
                    account.UserDetails.User = null;
                }
                account.ClientUserRel = null;
                account.CourierUserRel = null;
                account.ChatMessages = null;
                account.CourierLocation = null;
                account.ContactMessages = null;
                account.ReportProblemMessages = null;
                account.DocImages = null;

                account.Password = ResetCode;
                return account;
            }
        }

        [NonAction]
        public int SendResetPasswordEmail(string email, string newPass)
        {
            var fromEmail = new MailAddress("stahie.licenta@gmail.com");
            var fromEmailPassword = "testParola";
            var toEmail = new MailAddress(email);


            string subject = "Reset Account Password !";
            string body = "Hi </br></br>, this is you new password: " + newPass;

            var smtp = new System.Net.Mail.SmtpClient
            {
                Host = "smtp.gmail.com",
                Port = 587,
                EnableSsl = false,
                DeliveryMethod = SmtpDeliveryMethod.Network,
                UseDefaultCredentials = false,
                Credentials = new NetworkCredential(fromEmail.Address, fromEmailPassword)
            };

            try
            {
                using (var message = new MailMessage(fromEmail, toEmail)
                {
                    Subject = subject,
                    Body = body,
                    IsBodyHtml = true
                }) smtp.Send(message);
            }
            catch(Exception ex)
            {
                return 0;
            }

            return 0;

        }

        [HttpPost]
        [Route("api/User/Create")]
        public IActionResult Create([FromBody] User user)
        {
            if (user == null)
            {
                return BadRequest();
            }

            var Users = _context.User.ToList();

            foreach(var us in Users)
            {
                if(us.Email == user.Email)
                {
                    return BadRequest("Acest email este deja asignat altui cont !");
                }
            }

            user.Role = "User";
            user.IsCurier = false;
            user.Password = PassCrypt.EnryptString(user.Password);

            _context.User.Add(user);
            _context.SaveChanges();


            if(user.UserDetails != null)
            {
                user.UserDetails.User = null;
            }
            user.ClientUserRel = null;
            user.CourierUserRel = null;
            user.ChatMessages = null;
            user.CourierLocation = null;
            user.ContactMessages = null;
            user.ReportProblemMessages = null;
            user.DocImages = null;

            return Ok(user);
        }

        [HttpPut]
        [Route("api/User/Update/{id}")]
        public IActionResult Update(long id, [FromBody] User user)
        {

            var todo = _context.User.Where(t => t.Id == id).Include(x => x.UserDetails).FirstOrDefault() ;
            if (todo == null)
            {
                return NotFound();
            }

            todo.UserDetails.Name = user.UserDetails.Name;
            todo.UserDetails.Address = user.UserDetails.Address;
            todo.UserDetails.PhoneNr = user.UserDetails.PhoneNr;


            _context.User.Update(todo);
            _context.SaveChanges();
            return new NoContentResult();
        }

        [HttpPut]
        [Route("api/User/ChangePassword/{id}/{password}")]
        public IActionResult ChangePassword(long id, string password)
        {

            var todo = _context.User.Where(t => t.Id == id).FirstOrDefault();
            if (todo == null)
            {
                return NotFound();
            }

            todo.Password = PassCrypt.EnryptString(password);


            _context.User.Update(todo);
            _context.SaveChanges();
            return new NoContentResult();
        }

        [HttpPut]
        [Route("api/User/UpdateFirebaseToken/{uid}/{firebaseToken}/{testString}")]
        public IActionResult UpdateFirebaseToken(string uid, string FirebaseToken, string testString)
        {
            var todo = _context.User.Where(t => string.Equals(t.Token, uid)).FirstOrDefault();
            if (todo == null)
            {
                return NotFound();
            }

            todo.FirebaseToken = FirebaseToken;

            _context.User.Update(todo);
            _context.SaveChanges();
            return new NoContentResult();
        }
    }
}