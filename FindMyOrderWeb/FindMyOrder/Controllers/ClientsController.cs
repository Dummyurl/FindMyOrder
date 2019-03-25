using FindMyOrder.Hashing;
using FindMyOrder.Models;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace FindMyOrder.Controllers
{
    public class ClientsController : Controller
    {
        public ActionResult Index()
        {
            return View();
        }

        [HttpPost]
        public ActionResult GetClients()
        {
            List<User> usersList = new List<User>();

            using (MyDBContext dbc = new MyDBContext())
            {
                var myUserList = dbc.User.Where(x => x.IsCurier == false && x.Role != "admin").Include(x => x.UserDetails).ToList();

                foreach (var user in myUserList)
                {
                    user.UserDetails.User = null;
                }

                usersList = myUserList;
            }

            usersList.OrderBy(x => x.UserDetails.Name).ToList();

            return Json(new { data = usersList }, JsonRequestBehavior.AllowGet);
        }

        [HttpGet]
        public ActionResult SaveOrUpdate(int id)
        {
            if (id != -1) //update
            {
                using (MyDBContext dc = new MyDBContext())
                {
                    var v = dc.User.Where(a => a.Id == id && a.IsCurier == false && a.Role != "admin").Include(x => x.UserDetails).FirstOrDefault();
                    v.Password = PassCrypt.DecryptString(v.Password);
                    return View(v);
                }
            }
            else // create 
            {
                User v = new User();
                v.Id = -1;
                return View(v);
            }
        }

        [HttpPost]
        public ActionResult SaveOrUpdate(User model)
        {
            if (ModelState.IsValid)
            {
                using (MyDBContext dc = new MyDBContext())
                {
                    if (model.Id > 0) //Update
                    {
                        var original = dc.User.Where(a => a.Id == model.Id && a.IsCurier == false && a.Role != "admin").Include(x => x.UserDetails).FirstOrDefault();
                        if (original != null)
                        {
                            model.IsCurier = false;
                            model.Role = "user";
                            model.Password = PassCrypt.EnryptString(model.Password);
                            dc.Entry(original).CurrentValues.SetValues(model);
                            dc.Entry(original.UserDetails).CurrentValues.SetValues(model.UserDetails);
                        }
                    }
                    else //Save
                    {
                        model.IsCurier = false;
                        model.Role = "user";
                        model.Id = 0;
                        model.Password = PassCrypt.EnryptString(model.Password);
                        dc.User.Add(model);
                    }
                    dc.SaveChanges();
                }
            }

            return View("Index");
        }

        [HttpGet]
        public ActionResult Delete(int id)
        {
            using (MyDBContext dc = new MyDBContext())
            {
                var v = dc.User.Where(a => a.Id == id && a.IsCurier == false && a.Role != "admin").Include(x => x.UserDetails).FirstOrDefault();
                if (v != null)
                {
                    v.Password = PassCrypt.DecryptString(v.Password);
                    return View(v);
                }
                else
                {
                    return HttpNotFound();
                }
            }
        }

        [HttpPost]
        public ActionResult Delete(User model)
        {
            using (MyDBContext dc = new MyDBContext())
            {
                var v = dc.User.Where(a => a.Id == model.Id && a.IsCurier == false && a.Role != "admin").Include(x => x.UserDetails).FirstOrDefault();
                if (v != null)
                {
                 
                    //delete contacts messages
                    var contactMessages = dc.Contact.Where(x => x.UserId == model.Id).ToList();
                    if(contactMessages.Count > 0)
                    {
                        foreach(var contact in contactMessages)
                        {
                            dc.Contact.Remove(contact);
                        }
                    }


                    //delete userRelationship
                    var userRel = dc.UserRelationship.Where(x => x.ClientId == model.Id).ToList();
                    foreach (var rel in userRel)
                    {
                        dc.UserRelationship.Remove(rel);
                    }


                    //delete client packages
                    var userPack = dc.ClientPackage.Where(x => x.ClientId == model.Id).ToList();
                    foreach (var rel in userPack)
                    {
                        dc.ClientPackage.Remove(rel);
                    }

                    dc.UserDetails.Remove(v.UserDetails);
                    dc.User.Remove(v);



                    dc.SaveChanges();
                }

            }
            return View("Index");
        }
    }
}