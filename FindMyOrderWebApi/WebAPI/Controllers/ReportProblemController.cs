using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using WebAPI.Models;

namespace WebAPI.Controllers
{
    public class ReportProblemController : Controller
    {
        private readonly MyDbContext _context;

        public ReportProblemController(MyDbContext context)
        {
            _context = context;
        }

        [HttpPost]
        [Route("api/ReportProblem/Create")]
        public IActionResult Create([FromBody] ReportProblemForm reportProblemForm)
        {
            var user = _context.User.Where(x => x.Id == reportProblemForm.UserId).FirstOrDefault();

            if (user != null)
            {
                _context.ReportProblemForm.Add(reportProblemForm);
                _context.SaveChanges();
                return Ok("Va multumim pentru raportarea problemei, vom incerca sa gasim o solutie in cel mai scurt timp posibil.");
            }
            else
            {
                return BadRequest("Ne pare rau dar a aparut o eroare in raportarea problemei, incercati din nou!");
            }
        }
    }
}