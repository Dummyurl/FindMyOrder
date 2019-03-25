using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using WebAPI.Models;
using System.Text;
using System.IO;
using static System.Net.Mime.MediaTypeNames;

namespace WebAPI.Controllers
{
    public class DocumentPhotosController : Controller
    {
        private readonly MyDbContext _context;

        public DocumentPhotosController(MyDbContext context)
        {
            _context = context;
        }

        [HttpPost]
        [Route("api/DocumentPhotos/Create")]
        public IActionResult Create([FromBody] DocumentPhotos docPhoto)
        {
            try
            {
                docPhoto.DocImage = Encoding.ASCII.GetBytes(docPhoto.DocStringImage);
            }
            catch (Exception ex) { }


            if (docPhoto != null)
            {
                _context.DocumentPhotos.Add(docPhoto);
                _context.SaveChanges();
                return Ok("Imaginea a fost salvata cu succes!");
            }
            else
            {
                return BadRequest("Ne pare rau dar a aparut o eroare, incercati din nou!");
            }
        }


        [HttpGet]
        [Route("api/DocumentPhotos/GetAllPicturesByClientId/{Id}")]
        public Dictionary<string, List<DocumentPhotos>> GetAllPicturesByClientId(int Id)
        {
            List<DocumentPhotos> docPhotos = _context.DocumentPhotos.Where(x => x.ClientId == Id).ToList();

            if (docPhotos != null)
            {
                foreach (var pic in docPhotos)
                {
                    pic.DocStringImage = System.Text.Encoding.Default.GetString(pic.DocImage);
                }

                Dictionary<string, List<DocumentPhotos>> myDictionary = new Dictionary<string, List<DocumentPhotos>>();
                myDictionary.Add("DocumentPhotos", docPhotos);

                return myDictionary;
            }
            else
            {
                return new Dictionary<string, List<DocumentPhotos>>();
            }

        }

    }
}