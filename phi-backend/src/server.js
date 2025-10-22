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


app.post("/api/upload", upload.single("audio"), async (req, res) => {
  if (!req.file) return res.status(400).json({ error: "No file uploaded" });

  const inputPath = path.resolve(req.file.path);
  const monoPath = path.resolve("uploads", `${req.file.filename}_mono.wav`);

  try {
    // stt needs mono file, weirdly nice ffmpeg library. Like better than native ffmpeg
    await new Promise((resolve, reject) => {
      ffmpeg(inputPath)
        .audioChannels(1)
        .toFormat("wav")
        .save(monoPath)
        .on("end", resolve)
        .on("error", reject);
    });

    const transcription = await googleSTT(monoPath);
    console.log(transcription);

    fs.unlinkSync(inputPath);
    fs.unlinkSync(monoPath);

    res.json({ transcription });
  } catch (err) {
    console.error("STT error:", err);
    res.status(500).json({ error: "Speech recognition failed", details: err.message });
  }
});


// listen forever
app.listen(port, () => {
  console.log(`App listening on port ${port}`)
})
