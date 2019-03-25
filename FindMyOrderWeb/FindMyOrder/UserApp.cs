using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Web;

namespace FindMyOrder
{
    public class UserApp : ClaimsPrincipal
    {
        public UserApp(ClaimsPrincipal principal)
        : base(principal)
        {
        }

        public string Email
        {
            get
            {
                return this.FindFirst(ClaimTypes.Email).Value;
            }
        }

        public string Role
        {
            get
            {
                return this.FindFirst(ClaimTypes.Role).Value;
            }
        }

        public string UserId
        {
            get
            {
                return this.FindFirst(ClaimTypes.UserData).Value;
            }
        }
    }
}