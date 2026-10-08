import "./Card.css";

function InfoCard({ title, content, author }) {
  return (
    <div className="card">
      <h2>{title}</h2>
      <p>{content}</p>
      <p>Author: {author}</p>
    </div>
  );
}

export default InfoCard; 
