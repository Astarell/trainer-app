// assets/js/auth.js

async function register() {
    const email = document.getElementById('regEmail').value.trim();
    const firstName = document.getElementById('regFirstName').value.trim();
    const lastName = document.getElementById('regLastName').value.trim();
    const password = document.getElementById('regPass').value.trim();
    const userRole = document.getElementById('regRole').value;

    if (!email || !firstName || !lastName || !password) {
        alert('Заполни все поля!');
        return;
    }

    try {
        const res = await fetch('http://localhost:8080/api/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, firstName, lastName, password, userRole })
        });

        if (!res.ok) throw new Error(await res.text());

        const data = await res.json();
        localStorage.setItem('token', data.token);
        alert('✅ Регистрация успешна!');
        window.location.href = 'index.html';
    } catch (e) {
        alert('❌ ' + e.message);
    }
}

async function login() {
    const email = document.getElementById('loginEmail').value.trim();
    const password = document.getElementById('loginPass').value.trim();

    if (!email || !password) {
        alert('Email и пароль обязательны!');
        return;
    }

    try {
        const res = await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        if (!res.ok) throw new Error(await res.text());

        const data = await res.json();
        localStorage.setItem('token', data.token);
        alert('✅ Вход выполнен!');
        window.location.href = 'index.html';
    } catch (e) {
        alert('❌ ' + e.message);
    }
}

function logout() {
    localStorage.removeItem('token');
    alert('👋 Вы вышли');
    window.location.href = 'login.html';
}

window.register = register;
window.login = login;
window.logout = logout;