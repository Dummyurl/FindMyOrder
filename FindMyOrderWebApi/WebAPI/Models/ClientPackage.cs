using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebAPI.Models
{
    public class ClientPackage
    {
        public int Id { get; set; }

        public string Name { get; set; }

        public int ClientId { get; set; }
        //public User Client { get; set; }


        public UserRelationship UserRelationship { get; set; }

    }
}
