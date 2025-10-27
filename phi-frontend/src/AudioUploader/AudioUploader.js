import { useState } from "react";


const getApiBaseUrl = () => {
  if (window.location.hostname === "localhost") return "http://localhost:3001";
  return ""; // same origin in production
};

export default function AudioUploader() {
  const [file, setFile] = useState(null);

  const handleFileChange = e => setFile(e.target.files[0]);

  const handleUpload = async () => {
    if (!file) return alert("Select an audio file first");

    const formData = new FormData();
    formData.append("audio", file);

    try {

      const fullURL = `${getApiBaseUrl()}/api/upload`;
      console.log("I am about to send a req to " + fullURL);

      const res = await fetch(fullURL, {
        method: "POST",
        body: formData,
      });


      const data = await res.json();
      console.log("Server response:", data);
    } catch (err) {
      console.error("Upload failed:", err);
    }
  };

  return (
    <div className="p-4">
      <input type="file" accept="audio/*" onChange={handleFileChange} />
      <button onClick={handleUpload}>Upload</button>
    </div>
  );
}
