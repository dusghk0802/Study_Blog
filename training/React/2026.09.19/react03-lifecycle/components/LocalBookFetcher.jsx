import { useState, useEffect } from 'react'

function LocalBookFetcher() {
  const [books, setBooks] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchBooks = async () => {
      try {
        const response = await fetch('/json/books.json')
        const data = await response.json()
        setBooks(data)
      } catch (error) {
        console.error('Failed to fetch books:', error)
      } finally {
        setLoading(false)
      }
    }

    fetchBooks()
  }, [])

  if (loading) return <p>Loading...</p>

  return (
    <>
      <h2>내부통신1</h2>

      <ul>
        {books.map((book) => (
          <li key={book.id}>
            <strong>{book.title}</strong> by {book.author}
          </li>
        ))}
      </ul>
    </>
  )
}

export default LocalBookFetcher