using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace FindMyOrder.Models
{
    [Table("ReportProblemForm")]
    public class ReportProblemForm
    {
        public int Id { get; set; }
        public string Message { get; set; }

        public int UserId { get; set; }
        public User User { get; set; }
    }
}