﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebAPI.Models
{
    public class ReportProblemForm
    {
        public int Id { get; set; }
        public string Message { get; set; }

        public int UserId { get; set; }
        public User User { get; set; }
    }
}
