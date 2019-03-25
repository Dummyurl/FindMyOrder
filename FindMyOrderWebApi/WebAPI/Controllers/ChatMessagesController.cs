using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using WebAPI.Models;
using Microsoft.EntityFrameworkCore;

namespace WebAPI.Controllers
{
    //[Route("api/ChatMessages")]
    public class ChatMessagesController : Controller
    {
        private readonly MyDbContext _context;

        public ChatMessagesController(MyDbContext context)
        {
            _context = context;
        }

        [HttpPost]
        [Route("api/ChatMessages/SaveMessageToDatabase/{senderId}/{senderMessage}/{receiverId}")]
        public IActionResult SaveMessageToDatabase(int senderId, string senderMessage, int receiverId)
        {

            ChatMessages message = new ChatMessages();
            message.SenderId = senderId;
            message.ReceiverId = receiverId;
            message.ChatMessage = senderMessage;
            message.ChatTime = DateTime.Now;

            _context.ChatMessages.Add(message);
            _context.SaveChanges();

            return Ok();

        }

    }
}