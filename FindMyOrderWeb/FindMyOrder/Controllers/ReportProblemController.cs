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
    public class ReportProblemController : Controller
    {
        // GET: ReportProblem
        public ActionResult Index()
        {
            return View();
        }

        [HttpPost]
        public ActionResult GetProblemMessages()
        {
            List<ReportProblemForm> problemMessagesList = new List<ReportProblemForm>();
            using (MyDBContext dbc = new MyDBContext())
            {
                var myProblemList = dbc.ReportProblemForm.Where(x => x.User.IsCurier == true && x.User.Role != "admin").Include(x => x.User).Include(y => y.User.UserDetails).ToList();

                foreach (var problem in myProblemList)
                {
                    problem.User.ContactMessages = null;
                    problem.User.ProblemMessages = null;
                    problem.User.UserDetails.User = null;
                    problem.User.ClientPackages = null;
                }
                problemMessagesList = myProblemList;
            }
            problemMessagesList.OrderBy(x => x.User.UserDetails.Name).ToList();

            return Json(new { data = problemMessagesList }, JsonRequestBehavior.AllowGet);
        }
    }
}