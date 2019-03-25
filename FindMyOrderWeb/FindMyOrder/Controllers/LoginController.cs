using FindMyOrder.Hashing;
using FindMyOrder.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Web;
using System.Web.Mvc;

namespace FindMyOrder.Controllers
{
    [AllowAnonymous]
    public class LoginController : Controller
    {

        [HttpGet]
        public ActionResult LogIn(string returnUrl)
        {
            User model = new User()
            {
                ReturnUrl = returnUrl
            };
            return View();
        }

        [HttpPost]
        public ActionResult LogIn(User model)
        {
            User findAdmin = new User();

            using (var context = new MyDBContext())
            {
                string pass = "test123";
                string encryptPass = PassCrypt.EnryptString(pass);
                findAdmin = context.User.Where(x => x.Email == "admin@admin.com" && x.Password == encryptPass && x.Role == "admin").FirstOrDefault();
            }
            if (model.Email == findAdmin.Email && PassCrypt.EnryptString(model.Password) == findAdmin.Password)
            {
                try
                {
                    var identity = new ClaimsIdentity(new[] {
                    new Claim(ClaimTypes.Email, findAdmin.Email),
                    new Claim(ClaimTypes.Role, findAdmin.Role),
                    new Claim(ClaimTypes.UserData, findAdmin.Id.ToString()),
                    }, "ApplicationCookie");

                    var ctx = Request.GetOwinContext();
                    var authManager = ctx.Authentication;

                    authManager.SignIn(identity);

                    return Redirect(GetRedirectUrl(model.ReturnUrl));
                }
                catch (Exception) { }
            }

            //auth fail
            ModelState.AddModelError("Error", "Username sau parola incorecte");
            return View();
        }

        private string GetRedirectUrl(string returnUrl)
        {
            if (string.IsNullOrEmpty(returnUrl) || !Url.IsLocalUrl(returnUrl))
            {
                return Url.Action("Index", "Admin");
            }

            return returnUrl;
        }

        public ActionResult LogOut()
        {
            var ctx = Request.GetOwinContext();
            var authManager = ctx.Authentication;

            authManager.SignOut("ApplicationCookie");
            return RedirectToAction("LogIn", "Login");
        }
    }
}