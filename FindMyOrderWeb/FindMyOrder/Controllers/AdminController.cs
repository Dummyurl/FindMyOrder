using FindMyOrder.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace FindMyOrder.Controllers
{
    [Authorize(Roles = "admin")]
    public class AdminController : Controller
    {
        public ActionResult Index()
        {
            AllIndexInfo indexInfo = new AllIndexInfo();

            using (MyDBContext dc = new MyDBContext())
            {
                indexInfo.AllClients = dc.User.Where(a => a.IsCurier == false && a.Role != "admin").Count();
                indexInfo.AllCouriers = dc.User.Where(a => a.IsCurier == true && a.Role != "admin").Count();
                indexInfo.AllContactMessages = dc.Contact.Where(a => a.User.IsCurier == false && a.User.Role != "admin").Count();
                indexInfo.ALLProblemReported = dc.ReportProblemForm.Where(a => a.User.IsCurier == true && a.User.Role != "admin").Count();
            }

            return View(indexInfo);
        }
    }
}