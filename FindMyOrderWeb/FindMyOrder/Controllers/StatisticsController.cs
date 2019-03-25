using FindMyOrder.Models;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Data.Entity.Spatial;
using System.Device.Location;
using System.Linq;
using System.Web.Mvc;

namespace FindMyOrder.Controllers
{
    public class StatisticsController : Controller
    {
        // GET: Statistics
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
                var myUserList = dbc.User.Where(x => x.IsCurier == true && x.Role != "admin").Include(x => x.UserDetails).ToList();

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
        public ActionResult CheckStats(int id)
        {
            List<CourierLocation> courierLocation = new List<CourierLocation>();
            StatsModel statModel = new StatsModel();
            var measuresDate = DateTime.Now;
            using (MyDBContext dbc = new MyDBContext())
            {
                var myLocationList = dbc.CourierLocation.Where(x => x.CourierId == id && x.LocationTime.Year == measuresDate.Year && x.LocationTime.Month == measuresDate.Month && x.LocationTime.Day == measuresDate.Day).Include(x => x.Courier).ToList();

                foreach (var user in myLocationList)
                {
                    user.Courier.UserDetails.User = null;
                }

                courierLocation = myLocationList;


                if (courierLocation.Count == 0)
                {
                    CourierLocation firstLoc = new CourierLocation()
                    {
                        CourierId = id
                    };
                    courierLocation.Add(firstLoc);
                }

                foreach (var val in courierLocation)
                {
                    val.StringLocationTime = val.LocationTime.ToString("hh:mm");
                }

                statModel.CourierLocations = courierLocation;
                statModel.CourierId = id;
                statModel.ChosenDate = DateTime.Now;


                statModel.NrOfDeliveredPackages = dbc.DeliveredPackages.Where(x => x.CourierId == id && x.SubmitTime.Year == measuresDate.Year && x.SubmitTime.Month == measuresDate.Month && x.SubmitTime.Day == measuresDate.Day).Count(); ;


                List<String> myObsList = dbc.DeliveredPackages.Where(x => x.CourierId == id && x.SubmitTime.Year == measuresDate.Year && x.SubmitTime.Month == measuresDate.Month && x.SubmitTime.Day == measuresDate.Day).Select(z => z.Observations).ToList();
                statModel.Observations = "";
                if (myObsList.Count > 0)
                {
                    foreach (var obs in myObsList)
                    {
                        statModel.Observations += obs.ToString() + ", ";
                    }
                }
               
                double totalDistance = 0;
                for(var i = 0; i< statModel.CourierLocations.Count - 1; i++)
                {
                    var sCoord = new GeoCoordinate(statModel.CourierLocations[i].Latitude, statModel.CourierLocations[i].Longitude);
                    var eCoord = new GeoCoordinate(statModel.CourierLocations[i+1].Latitude, statModel.CourierLocations[i+1].Longitude);

                    var distanceAB = sCoord.GetDistanceTo(eCoord); //in m
                    totalDistance += distanceAB;
                }

                //transform from m to km
                statModel.Mileage = totalDistance / 1000;
                statModel.Mileage = Math.Round(statModel.Mileage, 2);

                var CarFuelConsumption = dbc.User.Where(x => x.Id == id).Include(z => z.Car).Select(y => y.Car.LPerHundredKm).FirstOrDefault();
                statModel.AproxSpentFuel = (Convert.ToDouble(CarFuelConsumption) * statModel.Mileage) / 100;
                statModel.AproxSpentFuel = Math.Round(statModel.AproxSpentFuel, 2);

            }
            return View(statModel);
        }

        public JsonResult GetLocationsByDate(string chosenDate, int courierId)
        {
            DateTime myDateTime;
            DateTime.TryParse(chosenDate, out myDateTime);

            List<CourierLocation> courierLocation = new List<CourierLocation>();
            StatsModel statModel = new StatsModel();

            var measuresDate = myDateTime;
            using (MyDBContext dbc = new MyDBContext())
            {
                var myLocationList = dbc.CourierLocation.Where(x => x.CourierId == courierId && x.LocationTime.Year == measuresDate.Year && x.LocationTime.Month == measuresDate.Month && x.LocationTime.Day == measuresDate.Day).Include(x => x.Courier).ToList();

                List<String> myObsList = dbc.DeliveredPackages.Where(x => x.CourierId == courierId && x.SubmitTime.Year == measuresDate.Year && x.SubmitTime.Month == measuresDate.Month && x.SubmitTime.Day == measuresDate.Day).Select(z => z.Observations).ToList();
                statModel.Observations = "";
                if (myObsList.Count > 0)
                {
                    foreach (var obs in myObsList)
                    {
                        statModel.Observations += obs.ToString() + ", ";
                    }
                }

                foreach (var user in myLocationList)
                {
                    user.Courier = null;
                }

                courierLocation = myLocationList;

                if (courierLocation.Count == 0)
                {
                    CourierLocation firstLoc = new CourierLocation()
                    {
                        CourierId = courierId
                    };
                    courierLocation.Add(firstLoc);
                }

                foreach(var val in courierLocation)
                {
                    val.StringLocationTime = val.LocationTime.ToString("hh:mm");
                }

                statModel.CourierLocations = courierLocation;
                statModel.CourierId = courierId;
                statModel.ChosenDate = DateTime.Now;

                statModel.NrOfDeliveredPackages = dbc.DeliveredPackages.Where(x => x.CourierId == courierId && x.SubmitTime.Year == measuresDate.Year && x.SubmitTime.Month == measuresDate.Month && x.SubmitTime.Day == measuresDate.Day).Count();

                double totalDistance = 0;
                for (var i = 0; i < statModel.CourierLocations.Count - 1; i++)
                {
                    var sCoord = new GeoCoordinate(statModel.CourierLocations[i].Latitude, statModel.CourierLocations[i].Longitude);
                    var eCoord = new GeoCoordinate(statModel.CourierLocations[i + 1].Latitude, statModel.CourierLocations[i + 1].Longitude);

                    var distanceAB = sCoord.GetDistanceTo(eCoord); //in m
                    totalDistance += distanceAB;
                }
                //transform from m to km
                statModel.Mileage = totalDistance / 1000;
                statModel.Mileage = Math.Round(statModel.Mileage, 2);

                var CarFuelConsumption = dbc.User.Where(x => x.Id == courierId).Include(z => z.Car).Select(y => y.Car.LPerHundredKm).FirstOrDefault();
                statModel.AproxSpentFuel = (Convert.ToDouble(CarFuelConsumption) * statModel.Mileage)/100;
                statModel.AproxSpentFuel = Math.Round(statModel.AproxSpentFuel, 2);

            }
            return Json(statModel, JsonRequestBehavior.AllowGet);
        }
    }
}