﻿using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace FindMyOrder.Models
{
    [Table("UserRelationship")]
    public class UserRelationship
    {
        public int Id { get; set; }
        public int ClientId { get; set; }
        public User Client { get; set; }

        public int CourierId { get; set; }
        public User Courier { get; set; }

        public int ClientPackageId { get; set; }
        public ClientPackage ClientPackage { get; set; }


        public bool Valid { get; set; }
    }
}