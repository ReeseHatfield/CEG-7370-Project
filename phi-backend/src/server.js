import express from "express";
import multer from "multer";
import cors from "cors";
import path from "path";
import fs from "fs";
import { googleSTT } from "./components/googleSTT.js";
import ffmpeg from "fluent-ffmpeg";

const app = express()
const port = 3001

app.use(cors());

app.get('/', (req, res) => {
  res.send('Hello World!')
})


const uploadDir = "./uploads";
if (!fs.existsSync(uploadDir)) fs.mkdirSync(uploadDir);

// ripped from multer disk storage sample docs
const storage = multer.diskStorage({
  destination: (_, __, cb) => cb(null, uploadDir),
  filename: (_, file, cb) => cb(null, Date.now() + path.extname(file.originalname)),
});


const args = process.argv.slice(2); 


console.log("args: " + args)
// im so sorry jinho
let isRunningInProd = false;

if(args.length >= 1){
  
  if(args[0] == "prod"){
    isRunningInProd = true;
  }
}


let uploadRoute = ""
if (isRunningInProd){
  uploadRoute = "/upload";
}
else {
  uploadRoute = "/api/upload";
}


console.log(`Upload route was ${uploadRoute}`)

const upload = multer({ storage });
app.post(uploadRoute, upload.single("audio"), async (req, res) => {

  console.log("upload route was hit")
  const t0 = Date.now();
  try {
    console.log("received upload", req.file.path);
    const inputPath = path.resolve(req.file.path);
    const monoPath = path.resolve("uploads", `${req.file.filename}_mono.wav`);

    await new Promise((resolve, reject) => {
      ffmpeg(inputPath)
        .audioChannels(1)
        .toFormat("wav")
        .on("end", resolve)
        .on("error", reject)
        .save(monoPath);
    });
    console.log("ffmpeg done", Date.now() - t0);

    const transcription = await googleSTT(monoPath);
    console.log("STT done", Date.now() - t0, transcription);

    const response = await fetch("http://localhost:3002/rec", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ preferences: transcription }),
    });
    const recommendation = await response.text();
    console.log("rec API done", Date.now() - t0);

    res.json({ recommendation });
    console.log("response sent", Date.now() - t0);

    fs.unlinkSync(inputPath);
    fs.unlinkSync(monoPath);
  } catch (err) {
    console.error("upload flow error:", err);
    res.status(500).json({ error: err.message });
  }
});



// listen forever
app.listen(port, () => {
  console.log(`App listening on port ${port}`)
})
