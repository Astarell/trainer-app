const BASE_URL = 'http://localhost:8080/api';

let token = localStorage.getItem('token') || '';

export async function apiCall(method, endpoint, body = null) {
    console.log(`→ ${method} ${endpoint}`, body);   // <-- для отладки

    const headers = {
        'Content-Type': 'application/json',
        ...(token && { 'Authorization': `Bearer ${token}` })
    };

    try {
        const res = await fetch(BASE_URL + endpoint, {
            method,
            headers,
            body: body ? JSON.stringify(body) : null
        });

        console.log(`← ${res.status} ${endpoint}`);

        if (!res.ok) {
            const errText = await res.text();
            console.error('Ошибка ответа:', errText);
            throw new Error(errText || `HTTP ${res.status}`);
        }

        const data = await res.json();
        console.log('Успешный ответ:', data);
        return data;
    } catch (err) {
        console.error('Fetch error:', err);
        throw err;
    }
}

export function setToken(newToken) {
    token = newToken;
    localStorage.setItem('token', newToken);
}

export function logout() {
    localStorage.removeItem('token');
    window.location.href = 'login.html';
}