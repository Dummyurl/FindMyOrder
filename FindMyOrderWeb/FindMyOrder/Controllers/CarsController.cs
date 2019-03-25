using FindMyOrder.Models;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace FindMyOrder.Controllers
{
    public class CarsController : Controller
    {
        // GET: Cars
        public ActionResult Index()
        {
            return View();
        }

        [HttpPost]
        public ActionResult GetAllCars()
        {
            List<Cars> carsList = new List<Cars>();
            using (MyDBContext dbc = new MyDBContext())
            {
                carsList = dbc.Cars.ToList();
            }

            foreach(var car in carsList)
            {
                car.StringCarImage = "data:image/png;base64," + Convert.ToBase64String(car.CarImage, 0, car.CarImage.Length);
            }
             
            return Json(new { data = carsList }, JsonRequestBehavior.AllowGet);
        }

        [HttpGet]
        public ActionResult AddCar()
        {
            Cars car = new Cars();
            return View(car);
        }
        [HttpPost]
        public ActionResult AddCar(Cars model, HttpPostedFileBase image1)
        {
            using (var context = new MyDBContext())
            {
                if (image1 != null)
                {
                    model.CarImage = new byte[image1.ContentLength];
                    image1.InputStream.Read(model.CarImage, 0, image1.ContentLength);
                }
                context.Cars.Add(model);
                context.SaveChanges();
            }
            return RedirectToAction("Index", "Cars");
        }

        [HttpGet]
        public ActionResult UpdateCar(int id)
        {
            var car = new Cars();
            using (var context = new MyDBContext())
            {
                car = context.Cars.Where(x => x.Id == id).FirstOrDefault();
            }
            return View(car);
        }
        [HttpPost]
        public ActionResult UpdateCar(Cars model, HttpPostedFileBase image1)
        {
            using (var context = new MyDBContext())
            {
                var original = context.Cars.Where(a => a.Id == model.Id).FirstOrDefault();

                if (original != null)
                {
                    if(image1 != null)
                    {
                        model.CarImage = new byte[image1.ContentLength];
                        image1.InputStream.Read(model.CarImage, 0, image1.ContentLength);
                    }
                    else
                    {
                        model.CarImage = original.CarImage;
                    }
                    context.Entry(original).CurrentValues.SetValues(model);
                    context.SaveChanges();
                }
            }
            return RedirectToAction("Index", "Cars");

        }

        [HttpGet]
        public ActionResult DeleteCar(int id)
        {
            using (MyDBContext dc = new MyDBContext())
            {
                var v = dc.Cars.Where(a => a.Id == id).FirstOrDefault();
                if (v != null)
                {
                    v.StringCarImage = "data:image/png;base64," + Convert.ToBase64String(v.CarImage, 0, v.CarImage.Length);

                    return View(v);
                }
                else
                {
                    return HttpNotFound();
                }
            }
        }

        [HttpPost]
        public ActionResult DeleteCar(User model)
        {
            using (MyDBContext dc = new MyDBContext())
            {
                var car = dc.Cars.Where(a => a.Id == model.Id).FirstOrDefault();

                var allCars = dc.Cars.ToList();
                if(allCars.Count > 1)
                {
                    if (car != null)
                    {
                        var allusers = dc.User.Where(x => x.CarId == model.Id).Include(x => x.UserDetails).ToList();
                        allCars.Remove(car);
                        foreach(var user in allusers)
                        {
                            user.CarId = allCars.FirstOrDefault().Id;
                        }

                        car.Users = null;
                        dc.Cars.Remove(car);
                        dc.SaveChanges();
                    }
                }
            }
            return View("Index");
        }
    }
}