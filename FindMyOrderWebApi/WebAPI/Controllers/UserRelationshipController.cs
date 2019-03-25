using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using WebAPI.Models;
using Microsoft.EntityFrameworkCore;
using MoreLinq;

namespace WebAPI.Controllers
{
    //[Route("api/UserRelationship")]
    public class UserRelationshipController : Controller
    {
        private readonly MyDbContext _context;

        public UserRelationshipController(MyDbContext context)
        {
            _context = context;
        }

        [HttpGet]
        [Route("api/UserRelationship/GetAllCouriersByClientId/{id}")]
        public Dictionary<string, List<UserRelationship>> GetAllCouriersByClientId(int id)
        {

            var UsersRel = _context.UserRelationship.Where(x => x.ClientId == id && x.Valid == true).Include(x => x.ClientPackage).Include(x => x.Client).Include(x => x.Courier).Include(x => x.Client.UserDetails).Include(y => y.Courier.UserDetails).ToList();
            UsersRel = UsersRel.Where(x => x.Client.FirebaseToken != null && x.Courier.FirebaseToken != null).ToList();

            foreach(var x in UsersRel)
            {
                if(x.ClientPackage == null)
                {
                    x.ClientPackage = _context.ClientPackage.Where(y => y.Id == x.ClientPackageId).FirstOrDefault();
                }
            }

            foreach (var user in UsersRel)
            {
                try
                {
                    user.ClientPackage.UserRelationship = null;

                    user.Client.ClientUserRel = null;
                    user.Client.CourierUserRel = null;
                    user.Client.ChatMessages = null;
                    user.Client.CourierLocation = null;
                    user.Client.ContactMessages = null;
                    user.Client.ReportProblemMessages = null;
                    user.Client.DocImages = null;
                    user.Client.UserDetails.User = null;

                    user.Courier.ClientUserRel = null;
                    user.Courier.CourierUserRel = null;
                    user.Courier.ChatMessages = null;
                    user.Courier.CourierLocation = null;
                    user.Courier.ContactMessages = null;
                    user.Courier.ReportProblemMessages = null;
                    user.Courier.DocImages = null;
                    user.Courier.UserDetails.User = null;
                }

                catch (Exception ex) { }

                user.Courier.LastLatitude = _context.CourierLocation.Where(x => x.CourierId == user.CourierId).Select(x => x.Latitude).LastOrDefault();
                user.Courier.LastLongitude = _context.CourierLocation.Where(x => x.CourierId == user.CourierId).Select(x => x.Longitude).LastOrDefault();

            }

            foreach (var user in UsersRel)
            {
                if(user.ClientPackage!= null)
                {
                    user.Courier.ClientPackagesName.Add(user.ClientPackage.Name);
                }

            }

            var DistinctUsersRel = UsersRel.DistinctBy(i => i.CourierId).Where(x => x.ClientPackage != null).ToList();

            Dictionary<string, List<UserRelationship>> myDictionary = new Dictionary<string, List<UserRelationship>>();

            myDictionary.Add("UserRelationship", DistinctUsersRel);

            return myDictionary;
        }

        [HttpGet]
        [Route("api/UserRelationship/GetAllClientsBtCourierId/{id}")]
        public Dictionary<string, List<UserRelationship>> GetAllClientsBtCourierId(int id)
        {

            var UsersRel = _context.UserRelationship.Where(x => x.CourierId == id && x.Valid == true).Include(x => x.ClientPackage).Include(x => x.Client).Include(x => x.Courier).Include(x => x.Client.UserDetails).Include(y => y.Courier.UserDetails).ToList();
            UsersRel = UsersRel.Where(x => x.Client.FirebaseToken != null && x.Courier.FirebaseToken != null).ToList();

            foreach (var user in UsersRel)
            {
                try
                {
                    user.ClientPackage.UserRelationship = null;

                    user.Client.ClientUserRel = null;
                    user.Client.CourierUserRel = null;
                    user.Client.ChatMessages = null;
                    user.Client.ReportProblemMessages = null;
                    user.Client.CourierLocation = null;
                    user.Client.ContactMessages = null;
                    user.Client.DocImages = null;
                    user.Client.UserDetails.User = null;

                    user.Courier.ClientUserRel = null;
                    user.Courier.CourierUserRel = null;
                    user.Courier.ChatMessages = null;
                    user.Courier.CourierLocation = null;
                    user.Courier.ContactMessages = null;
                    user.Courier.ReportProblemMessages = null;
                    user.Courier.DocImages = null;
                    user.Courier.UserDetails.User = null;
                }

                catch (Exception ex) { }

            }

            foreach (var user in UsersRel)
            {
                user.Client.ClientPackagesName.Add(user.ClientPackage.Name);
            }

            var DistinctUsersRel = UsersRel.DistinctBy(i => i.ClientId).ToList();

            Dictionary<string, List<UserRelationship>> myDictionary = new Dictionary<string, List<UserRelationship>>();

            myDictionary.Add("UserRelationship", DistinctUsersRel);

            return myDictionary;
        }

        [HttpPut]
        [Route("api/UserRelationship/FinalizeDelivery")]
        public UserRelationship FinalizeDelivery([FromBody] FinalizeCommand finalCom)
        {
            var UsersRel = _context.UserRelationship.Where(x => x.CourierId == finalCom.CourierId && x.ClientId == finalCom.ClientId && x.Valid == true).ToList();

            if (UsersRel != null)
            {
                foreach (var usr in UsersRel)
                {
                    usr.Valid = false;
                    _context.UserRelationship.Update(usr);
                }
                _context.SaveChanges();

            }

            DeliveredPackages dp = new DeliveredPackages();
            dp.CourierId = finalCom.CourierId;
            dp.ClientId = finalCom.ClientId;
            dp.Observations = finalCom.Observations;
            dp.SubmitTime = finalCom.Date;

            _context.DeliveredPackages.Add(dp);
            _context.SaveChanges();

            return new UserRelationship();
        }
    }
}