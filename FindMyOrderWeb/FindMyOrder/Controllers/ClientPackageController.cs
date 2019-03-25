using FindMyOrder.Models;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace FindMyOrder.Controllers
{
    public class ClientPackageController : Controller
    {
        public ActionResult Index()
        {
            return View();
        }

        [HttpPost]
        public ActionResult GetClientPackages()
        {
            List<ClientPackage> clientPackageList = new List<ClientPackage>();
            using (MyDBContext dbc = new MyDBContext())
            {
                var myClientPackageList = dbc.ClientPackage.Where(x => x.User.IsCurier == false && x.User.Role != "admin").Include(x => x.User).Include(y => y.User.UserDetails).ToList();

                foreach (var pack in myClientPackageList)
                {
                    pack.User.ContactMessages = null;
                    pack.User.UserDetails.User = null;
                    pack.User.ProblemMessages = null;
                    pack.User.ClientPackages = null;
                }
                clientPackageList = myClientPackageList;
            }
            clientPackageList.OrderBy(x => x.Name).ToList();
            return Json(new { data = clientPackageList }, JsonRequestBehavior.AllowGet);
        }

        [HttpGet]
        public ActionResult SaveOrUpdate (int id)
        {
            if (id != -1) //update
            {
                using (MyDBContext dc = new MyDBContext())
                {
                    var pack = dc.ClientPackage.Where(a => a.Id == id ).Include(x => x.User).Include(x => x.User.UserDetails).FirstOrDefault();
                    return View(pack);
                }
            }
            else // create 
            {
                ClientPackage pack = new ClientPackage();
                pack.Id = -1;
                return View(pack);
            }
        }

        [HttpPost]
        public ActionResult SaveOrUpdate(ClientPackage model)
        {
            if (model.ClientId == 0)
            {
                TempData["msg"] = "<script>alert('Campul Client este obligatoriu!');</script>";
                return View(model);
            }
            else
            {
                if (ModelState.IsValid)
                {
                    using (MyDBContext dc = new MyDBContext())
                    {
                        if (model.Id > 0) //Update
                        {
                            var original = dc.ClientPackage.Where(a => a.Id == model.Id).FirstOrDefault();
                            if (original != null)
                            {
                                dc.Entry(original).CurrentValues.SetValues(model);
                            }
                        }
                        else //Save
                        {
                            dc.ClientPackage.Add(model);
                        }
                        dc.SaveChanges();
                    }
                }
                return View("Index");
            }
        }


        [HttpGet]
        public ActionResult ClientPackageTableDialog()
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
                    user.ClientPackages = null;
                    user.ContactMessages = null;
                    user.ProblemMessages = null;
                }

                usersList = myUserList;
            }


            usersList.OrderBy(x => x.UserDetails.Name).ToList();

            return Json(new { data = usersList }, JsonRequestBehavior.AllowGet);
        }

        [HttpGet]
        public ActionResult AjaxGetClientName(int id)
        {
            string clientName = "";
            using (MyDBContext dbc = new MyDBContext())
            {
                clientName = dbc.User.Where(x => x.Id == id).Include(x => x.UserDetails).Select(x => x.UserDetails.Name).FirstOrDefault();
            }

            return Json(new JsonResult()
            {
                Data = clientName
            }, JsonRequestBehavior.AllowGet);
        }


        [HttpGet]
        public ActionResult Delete(int id)
        {
            using (MyDBContext dc = new MyDBContext())
            {
                var v = dc.ClientPackage.Where(a => a.Id == id).Include(z => z.User).Include(x => x.User.UserDetails).FirstOrDefault();
                if (v != null)
                {
                    return View(v);
                }
                else
                {
                    return HttpNotFound();
                }
            }
        }

        [HttpPost]
        public ActionResult Delete(ClientPackage model)
        {
            using (MyDBContext dc = new MyDBContext())
            {
                var v = dc.ClientPackage.Where(a => a.Id == model.Id).FirstOrDefault();
                if (v != null)
                {

                    var userRel = dc.UserRelationship.Where(x => x.ClientPackageId == model.Id).ToList();
                    foreach (var rel in userRel)
                    {
                        dc.UserRelationship.Remove(rel);
                    }


                    dc.ClientPackage.Remove(v);
                    dc.SaveChanges();
                }

            }
            return View("Index");
        }
    }
}