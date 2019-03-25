using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using WebAPI.Models;

namespace WebAPI.Controllers
{
    //[Route("api/CourierLocation")]
    public class CourierLocationController : Controller
    {

        private readonly MyDbContext _context;

        public CourierLocationController(MyDbContext context)
        {
            _context = context;
        }

        [HttpPost]
        [Route("api/CourierLocation/SaveLocationToDatabase")]
        public IActionResult SaveLocationToDatabase([FromBody] CourierLocation location)
        {

            //location.LocationTime = DateTime.Now;

            try{
                _context.CourierLocation.Add(location);
                _context.SaveChanges();
            }
            catch(Exception ex) { }

            return Ok(location);

        }

        [HttpGet]
        [Route("api/CourierLocation/GetCourierLocationById/{id}")]
        public CourierLocation GetCourierLocationById(int id)
        {
            CourierLocation courierLocation = _context.CourierLocation.Where(x => x.CourierId == id).LastOrDefault();
           
            if(courierLocation != null)
            {
                return courierLocation;
            }
            else
            {
                return new CourierLocation();
            }

        }

    }
}