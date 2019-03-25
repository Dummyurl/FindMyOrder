using FindMyOrder.Models;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace FindMyOrder.Controllers
{
    public class UserRelationshipController : Controller
    {
        // GET: UserRelationship
        public ActionResult Index()
        {
            return View();
        }

        [HttpPost]
        public ActionResult GetUserRelationship()
        {
            List<UserRelationship> userRelationshipList = new List<UserRelationship>();
            using (MyDBContext dbc = new MyDBContext())
            {
                var myUserRelationshipList = dbc.UserRelationship
                    .Where(x => x.Valid == true)
                    .Include(x => x.ClientPackage)
                    .Include(x => x.Client)
                    .Include(x => x.Courier)
                    .Include(y => y.Client.UserDetails)
                    .Include(y => y.Courier.UserDetails)
                    .ToList();

                foreach (var rel in myUserRelationshipList)
                {
                    rel.Client.ContactMessages = null;
                    rel.Client.UserDetails.User = null;
                    rel.Client.ProblemMessages = null;
                    rel.Client.ClientPackages = null;
                    rel.Client.ClientUserRel = null;
                    rel.Client.CourierUserRel = null;

                    rel.Courier.ContactMessages = null;
                    rel.Courier.UserDetails.User = null;
                    rel.Courier.ProblemMessages = null;
                    rel.Courier.ClientPackages = null;
                    rel.Courier.ClientUserRel = null;
                    rel.Courier.CourierUserRel = null;

                    rel.ClientPackage.User = null;

                }
                userRelationshipList = myUserRelationshipList;
            }
            userRelationshipList.OrderBy(x => x.Client.UserDetails.Name).ToList();
            return Json(new { data = userRelationshipList }, JsonRequestBehavior.AllowGet);
        }

        [HttpGet]
        public ActionResult SaveOrUpdate(int id)
        {
            if (id != -1) //update
            {
                using (MyDBContext dc = new MyDBContext())
                {
                    var userRel = dc.UserRelationship.Where(a => a.Id == id)
                        .Include(x => x.ClientPackage)
                        .Include(x => x.Client)
                        .Include(x => x.Courier)
                        .Include(y => y.Client.UserDetails)
                        .Include(y => y.Courier.UserDetails)
                        .FirstOrDefault();
                    return View(userRel);
                }
            }
            else // create 
            {
                UserRelationship userRel = new UserRelationship();
                userRel.Id = -1;
                return View(userRel);
            }
        }

        [HttpPost]
        public ActionResult SaveOrUpdate(UserRelationship model)
        {
            //daca e ceva null mesaj de eroare
            if (model.ClientId == 0 || model.CourierId == 0 || model.ClientPackageId == 0)
            {
                TempData["msg"] = "<script>alert('Toate campurile sunt obligatorii!');</script>";
                return View(model);
            }
            else
            {
                if (ModelState.IsValid)
                {
                    using (MyDBContext dc = new MyDBContext())
                    {
                        //vf daca exista duplicat in baza, valid sau invalid
                        var duplicate = dc.UserRelationship
                            .Where(a => a.ClientId == model.ClientId && a.CourierId == model.CourierId && a.ClientPackageId == model.ClientPackageId)
                            .FirstOrDefault();

                        if (model.Id > 0) //Update
                        {
                            //daca editarea nu are duplicat in baza
                            if (duplicate == null)
                            {
                                //editam
                                var original = dc.UserRelationship.Where(a => a.Id == model.Id).FirstOrDefault();
                                if (original != null)
                                {
                                    model.Valid = true;
                                    dc.Entry(original).CurrentValues.SetValues(model);
                                }
                            }
                            else
                            {
                                if (duplicate.Valid == false)
                                {
                                    model.Valid = true;
                                    dc.Entry(duplicate).CurrentValues.SetValues(model);
                                }
                                else
                                {
                                    TempData["msgDuplicate"] = "<script>alert('Aceasta inregistrare exista deja in baza de date si este valida');</script>";
                                    return View(model);
                                }       
                            }        
                        }
                        else //Save
                        {
                            if(duplicate == null)
                            {
                                model.Valid = true;
                                dc.UserRelationship.Add(model);
                            }
                            else
                            {
                                //daca exista un duplicat dar e invalid, il validam si nu cream o noua inreg
                                if (duplicate.Valid == false)
                                {
                                    model.Valid = true;
                                    dc.Entry(duplicate).CurrentValues.SetValues(model);
                                }
                                else
                                {
                                    TempData["msgDuplicate"] = "<script>alert('Aceasta inregistrare exista deja in baza de date si este valida');</script>";
                                    return View(model);
                                }             
                            }
                        }
                        dc.SaveChanges();
                    }
                }
                return View("Index");
            }
        }

        #region SaveOrUpdate Ajaxs

        [HttpGet]
        public ActionResult UserRelClientTableDialog()
        {
            return View();
        }
        [HttpPost]
        public ActionResult GetUserRelClients()
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
        public ActionResult AjaxGetUserRelClientName(int id)
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
        public ActionResult UserRelCourierTableDialog()
        {
            return View();
        }
        [HttpPost]
        public ActionResult GetUserRelCouriers()
        {
            List<User> usersList = new List<User>();

            using (MyDBContext dbc = new MyDBContext())
            {
                var myUserList = dbc.User.Where(x => x.IsCurier == true && x.Role != "admin").Include(x => x.UserDetails).ToList();

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
        public ActionResult AjaxGetUserRelCourierName(int id)
        {
            string courierName = "";
            using (MyDBContext dbc = new MyDBContext())
            {
                courierName = dbc.User.Where(x => x.Id == id).Include(x => x.UserDetails).Select(x => x.UserDetails.Name).FirstOrDefault();
            }

            return Json(new JsonResult()
            {
                Data = courierName
            }, JsonRequestBehavior.AllowGet);
        }


        [HttpGet]
        public ActionResult UserRelClientPackageTableDialog()
        {
            return View();
        }
        [HttpPost]
        public ActionResult GetUserRelClientPackages(int clientId)
        {
            List<ClientPackage> clientPackageList = new List<ClientPackage>();

            if (clientId != 0)
            {
                using (MyDBContext dbc = new MyDBContext())
                {
                    var myclientPackageList = dbc.ClientPackage.Where(x => x.ClientId == clientId).Include(x => x.User.UserDetails).ToList();

                    foreach (var clientP in myclientPackageList)
                    {
                        clientP.User.UserDetails.User = null;
                        clientP.User.ClientPackages = null;
                        clientP.User.ContactMessages = null;
                        clientP.User.ProblemMessages = null;
                    }
                    clientPackageList = myclientPackageList;
                }

                clientPackageList.OrderBy(x => x.Name).ToList();
            }
            return Json(new { data = clientPackageList }, JsonRequestBehavior.AllowGet);
        }
        [HttpGet]
        public ActionResult AjaxGetUserRelClientPackageName(int id)
        {
            string clientPackageName = "";
            using (MyDBContext dbc = new MyDBContext())
            {
                clientPackageName = dbc.ClientPackage.Where(x => x.Id == id).Select(x => x.Name).FirstOrDefault();
            }

            return Json(new JsonResult()
            {
                Data = clientPackageName
            }, JsonRequestBehavior.AllowGet);
        }

        #endregion

        [HttpGet]
        public ActionResult Delete(int id)
        {
            using (MyDBContext dc = new MyDBContext())
            {
                var v = dc.UserRelationship.Where(a => a.Id == id && a.Valid == true)
                    .Include(x => x.ClientPackage)
                    .Include(x => x.Client)
                    .Include(x => x.Courier)
                    .Include(y => y.Client.UserDetails)
                    .Include(y => y.Courier.UserDetails)
                    .FirstOrDefault();

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
        public ActionResult Delete(UserRelationship model)
        {
            using (MyDBContext dc = new MyDBContext())
            {
                var v = dc.UserRelationship.Where(a => a.Id == model.Id).FirstOrDefault();
                if (v != null)
                {
                    dc.UserRelationship.Remove(v);
                    dc.SaveChanges();
                }
            }
            return View("Index");
        }
    }
}