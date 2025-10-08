import express from "express";
import multer from "multer";
import cors from "cors";
import path from "path";
import fs from "fs";
import { googleSTT } from "./components/googleSTT.js";


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

app.post("/upload", upload.single("audio"), async (req, res) => {
  if (!req.file) return res.status(400).json({ error: "No file uploaded" });

  try {
    const filePath = path.resolve(req.file.path);
    const transcription = await googleSTT(filePath);
    // res.json({
    //   message: "File uploaded and transcribed",
    //   filename: req.file.filename,
    //   transcription,
    // });


    console.log(transcription);
  } catch (err) {
    console.error("STT error:", err);
    res.status(500).json({ error: "Speech recognition failed", details: err.message });
  }
});



// listen forever
app.listen(port, () => {
  console.log(`App listening on port ${port}`)
})
