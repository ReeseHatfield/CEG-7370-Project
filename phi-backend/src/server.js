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





const upload = multer({ storage });

app.get("/api/upload", () => {
  console.log("you sent a GET to /api/upload");
})

app.post("/api/upload", upload.single("audio"), async (req, res) => {

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
app.listen(port, "0.0.0.0", () => {
  console.log(`App listening on port ${port}`)
})
