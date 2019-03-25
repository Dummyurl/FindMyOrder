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
    [Authorize]
    public class CouriersController : Controller
    {
        public ActionResult Index()
        {
            return View();
        }

        [HttpPost]
        public ActionResult GetCouriers()
        {
            List<User> usersList = new List<User>();

            using (MyDBContext dbc = new MyDBContext())
            {
                var myUserList = dbc.User.Where(x => x.IsCurier == true && x.Role != "admin").Include(x => x.UserDetails).Include(x => x.Car).ToList();

                foreach (var user in myUserList)
                {
                    user.UserDetails.User = null;
                    user.Car.Users = null;
                    user.Car.CarImage = null;
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
                    var v = dc.User.Where(a => a.Id == id && a.IsCurier == true && a.Role != "admin").Include(x => x.UserDetails).Include(x => x.Car).FirstOrDefault();
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
                        var original = dc.User.Where(a => a.Id == model.Id && a.IsCurier == true && a.Role != "admin").Include(x => x.UserDetails).FirstOrDefault();
                        if (original != null)
                        {
                            model.IsCurier = true;
                            model.Role = "user";
                            model.Password = PassCrypt.EnryptString(model.Password);
                            dc.Entry(original).CurrentValues.SetValues(model);
                            dc.Entry(original.UserDetails).CurrentValues.SetValues(model.UserDetails);
                        }
                    }
                    else //Save
                    {
                        model.IsCurier = true;
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
                var v = dc.User.Where(a => a.Id == id && a.IsCurier == true && a.Role != "admin").Include(x => x.UserDetails).Include(x => x.Car).FirstOrDefault();
                if (v != null)
                {
                    v.Car.StringCarImage = "data:image/png;base64," + Convert.ToBase64String(v.Car.CarImage, 0, v.Car.CarImage.Length);
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
                var v = dc.User.Where(a => a.Id == model.Id && a.IsCurier == true && a.Role != "admin").Include(x => x.UserDetails).FirstOrDefault();
                if (v != null)
                {
                    //delete reported problems
                    var problemReported = dc.ReportProblemForm.Where(x => x.UserId == model.Id).ToList();
                    foreach(var problem in problemReported)
                    {
                        dc.ReportProblemForm.Remove(problem);
                    }

                    //delete userRelationship
                    var userRel = dc.UserRelationship.Where(x => x.CourierId == model.Id).ToList();
                    foreach (var rel in userRel)
                    {
                        dc.UserRelationship.Remove(rel);
                    }

                    //delete user locations
                    var userLocations = dc.CourierLocation.Where(x => x.CourierId == model.Id).ToList();
                    foreach(var loc in userLocations)
                    {
                        dc.CourierLocation.Remove(loc);
                    }

                    //delete user and user details
                    dc.UserDetails.Remove(v.UserDetails);
                    dc.User.Remove(v);

                    dc.SaveChanges();
                }

            }
            return View("Index");
        }


        public ActionResult CarsTableDialog()
        {
            return View();
        }
        public ActionResult GetAllCarsInfo()
        {
            List<Cars> carsList = new List<Cars>();
            using (MyDBContext dbc = new MyDBContext())
            {
                carsList = dbc.Cars.ToList();
            }

            foreach (var car in carsList)
            {
                car.StringCarImage = "data:image/png;base64," + Convert.ToBase64String(car.CarImage, 0, car.CarImage.Length);
            }

            return Json(new { data = carsList }, JsonRequestBehavior.AllowGet);
        }
        [HttpGet]
        public JsonResult GetCarInfoAjax(int id)
        {
            string carName = "";
            using (MyDBContext dbc = new MyDBContext())
            {
                carName = dbc.Cars.Where(x => x.Id == id).Select(x => x.Name).FirstOrDefault();
            }

            return Json(new JsonResult()
            {
                Data = carName
            }, JsonRequestBehavior.AllowGet);
        }

    }
}