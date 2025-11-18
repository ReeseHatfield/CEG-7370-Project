import { useState } from "react";

const getApiBaseUrl = () => {
  if (window.location.hostname === "localhost") return "http://localhost:3001";
  return "";
};

export default function AudioUploader() {
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [responseText, setResponseText] = useState("");

  const handleFileChange = (e) => setFile(e.target.files[0]);

  const handleUpload = async () => {
    if (!file) return alert("Select an audio file first");

    const formData = new FormData();
    formData.append("audio", file);

    try {
      setLoading(true);
      setResponseText("");

      const fullURL = `${getApiBaseUrl()}/api/upload`;
      console.log("I am about to send a req to " + fullURL);

      const res = await fetch(fullURL, {
        method: "POST",
        body: formData,
      });

      const data = await res.json();

      setResponseText(data.recommendation || JSON.stringify(data, null, 2));
    } catch (err) {
      console.error("Upload failed:", err);
      setResponseText("Upload failed.");
    } finally {
      setLoading(false);
    }
  };

  return (
    // tailwind classes :0
    <div className="p-4 max-w-xl mx-auto space-y-4">
      <input type="file" accept="audio/*" onChange={handleFileChange} />

      <button onClick={handleUpload} disabled={loading}>
        {loading ? "Uploading..." : "Upload"}
      </button>

      {loading && (
        <div className="mt-4 text-gray-600 italic">Processing… please wait.</div>
      )}

      {/* nicely rendered response */}
      {responseText && (
        <div className="mt-4 p-4 border rounded bg-gray-100 whitespace-pre-wrap">
          {responseText}
        </div>
      )}
    </div>
  );
}
