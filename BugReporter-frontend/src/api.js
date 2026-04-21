const API = 'http://localhost:8080/api';

// ---- AUTH ----

export async function loginUser(username, password) {
  const res = await fetch(`${API}/users/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password })
  });
  if (!res.ok) {
    const err = await res.json();
    throw new Error(err.error || 'Eroare la login');
  }
  return res.json();
}

export async function registerUser(username, email, password) {
  const res = await fetch(`${API}/users/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, email, password })
  });
  if (!res.ok) {
    const err = await res.json();
    throw new Error(err.error || 'Eroare la inregistrare');
  }
  return res.json();
}

// ---- BUGS ----

export async function getBugs(params = {}) {
  const query = new URLSearchParams();
  if (params.authorId) query.append('authorId', params.authorId);
  if (params.tags) query.append('tags', params.tags);
  if (params.title) query.append('title', params.title);

  const res = await fetch(`${API}/bugs?${query.toString()}`);
  if (!res.ok) throw new Error('Eroare la incarcarea bugurilor');
  return res.json();
}

export async function getBugById(id) {
  const res = await fetch(`${API}/bugs/${id}`);
  if (!res.ok) throw new Error('Bug negasit');
  return res.json();
}

export async function createBug(bugData) {
  const res = await fetch(`${API}/bugs`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(bugData)
  });
  if (!res.ok) throw new Error('Eroare la crearea bugului');
  return res.json();
}

export async function updateBug(id, bugData) {
  const res = await fetch(`${API}/bugs/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(bugData)
  });
  if (!res.ok) throw new Error('Eroare la actualizarea bugului');
  return res.json();
}

export async function deleteBug(id) {
  const res = await fetch(`${API}/bugs/${id}`, { method: 'DELETE' });
  if (!res.ok) throw new Error('Eroare la stergerea bugului');
}

export async function markBugAsSolved(id) {
  const res = await fetch(`${API}/bugs/${id}/solve`, { method: 'PUT' });
  if (!res.ok) throw new Error('Eroare la marcarea bugului ca rezolvat');
  return res.json();
}

export async function voteBug(bugId, userId, voteType){
  const res=await fetch(`${API}/bugs/${bugId}/vote`,{
    method: 'POST',
    headers: {'Content-Type':'application/json'},
    body: JSON.stringify({userId, voteType})
  })
  if(!res.ok){
    const err=await res.text()
    throw new Error(err|| 'Eroare la votarea bugului');
  }
  return res.json();
}

// ---- COMMENTS ----

export async function getCommentsByBugId(bugId) {
  const res = await fetch(`${API}/bugs/${bugId}/comments`);
  if (!res.ok) throw new Error('Eroare la incarcarea comentariilor');
  return res.json();
}

export async function createComment(commentData) {
  const res = await fetch(`${API}/comments`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(commentData)
  });
  if (!res.ok) throw new Error('Eroare la adaugarea comentariului');
  return res.json();
}

export async function updateComment(id, commentData) {
  const res = await fetch(`${API}/comments/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(commentData)
  });
  if (!res.ok) throw new Error('Eroare la actualizarea comentariului');
  return res.json();
}

export async function deleteComment(id) {
  const res = await fetch(`${API}/comments/${id}`, { method: 'DELETE' });
  if (!res.ok) throw new Error('Eroare la stergerea comentariului');
}

export async function voteComment(commentId, userId, voteType) {
  const res = await fetch(`${API}/comments/${commentId}/vote`, {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify({userId, voteType})
  })
  if (!res.ok) {
    const err = await res.text()
    throw new Error(err || 'Eroare la votarea comentariului');
  }
  return res.json();
}

// ---- USERS ----

export async function getAllUsers() {
  const res = await fetch(`${API}/users`);
  if (!res.ok) throw new Error('Eroare la incarcarea utilizatorilor');
  return res.json();
}

// ---- FILES ----

export async function uploadFile(file) {
  const formData = new FormData();
  formData.append('file', file);
  const res = await fetch(`${API}/files/upload`, {
    method: 'POST',
    body: formData
  });
  if (!res.ok) throw new Error('Eroare la incarcarea fisierului');
  const data = await res.json();
  return data.url;
}
