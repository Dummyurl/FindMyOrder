using FindMyOrder.Models;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace FindMyOrder.Controllers
{
    [Authorize(Roles = "admin")]
    public class ContactController : Controller
    {
        // GET: Contact
        public ActionResult Index()
        {
            return View();
        }

        [HttpPost]
        public ActionResult GetContactMessages()
        {
            List<Contact> contactMessagesList = new List<Contact>();
            using (MyDBContext dbc = new MyDBContext())
            {
                var myContactList = dbc.Contact.Where(x => x.User.IsCurier == false && x.User.Role != "admin").Include(x => x.User).Include(y => y.User.UserDetails).ToList();

                foreach (var contact in myContactList)
                {
                    contact.User.ContactMessages = null;
                    contact.User.UserDetails.User = null;
                    contact.User.ProblemMessages = null;
                    contact.User.ClientPackages = null;
                }
                contactMessagesList = myContactList;
            }
            contactMessagesList.OrderBy(x => x.User.UserDetails.Name).ToList();
            return Json(new { data = contactMessagesList }, JsonRequestBehavior.AllowGet);
        }
    }
}