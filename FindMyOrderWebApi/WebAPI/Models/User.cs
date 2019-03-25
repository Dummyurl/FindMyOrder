using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace WebAPI.Models
{
    public class User
    {
        public int Id { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
        public string Token { get; set; }
        public string FirebaseToken { get; set; }
        public string Role { get; set; }
        public bool IsCurier { get; set; }

        [NotMapped]
        public List<string> ClientPackagesName { get; set; } = new List<string>();

        //NOT MAPPED
        [NotMapped]
        public string OldPassword { get; set; }
        [NotMapped]
        public double LastLatitude { get; set; }
        [NotMapped]
        public double LastLongitude { get; set; }

        

        public UserDetails UserDetails { get; set; }



        public ICollection<ChatMessages> ChatMessages { get; set; }
        //public ICollection<ClientPackage> ClientPackages { get; set; }

        public ICollection<CourierLocation> CourierLocation { get; set; }
        public ICollection<Contact> ContactMessages { get; set; }
        public ICollection<ReportProblemForm> ReportProblemMessages { get; set; }
        public ICollection<DocumentPhotos> DocImages { get; set; }



        public ICollection<UserRelationship> ClientUserRel { get; set; }
        public ICollection<UserRelationship> CourierUserRel { get; set; }



    }
}
