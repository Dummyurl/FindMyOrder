using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using WebAPI.Models;

namespace WebAPI.Controllers
{
    //[Route("api/Contact")]
    public class ContactController : Controller
    {

        private readonly MyDbContext _context;

        public ContactController(MyDbContext context)
        {
            _context = context;
        }

        [HttpPost]
        [Route("api/Contact/Create")]
        public IActionResult Create([FromBody] Contact contact)
        {
            var user = _context.User.Where(x => x.Id == contact.UserId).FirstOrDefault();

            if(user != null)
            {
                _context.Contact.Add(contact);
                _context.SaveChanges();
                return Ok("Mesajul a fost trimis cu succes! Va vom contacta in cel mai scurt timp pentru rezolvarea problemei. Va multumim!");
            }
            else
            {
                return BadRequest("Ne pare rau dar a aparut o eroare, incercati din nou!");
            }
        }
    }
}