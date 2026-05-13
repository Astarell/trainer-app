// assets/js/profile.js

async function loadProfile() {
    const container = document.getElementById('profileData');
    container.innerHTML = '<p class="text-slate-400">Загрузка профиля...</p>';

    try {
        const token = localStorage.getItem('token');
        if (!token) {
            container.innerHTML = `<p class="text-red-400">Вы не авторизованы</p>`;
            return;
        }

        const res = await fetch('http://localhost:8080/api/profile', {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (!res.ok) throw new Error('Ошибка сервера');

        const user = await res.json();
        console.log('✅ Профиль получен:', user);

        // Определение роли
        let role = 'Студент';
        let roleColor = 'text-cyan-400';

        if (user.roles && (user.roles.includes('expert') || user.roles.includes('APP_EXPERT'))) {
            role = 'Эксперт';
            roleColor = 'text-emerald-400';
        } else if (user.groups && user.groups.includes('expert')) {
            role = 'Эксперт';
            roleColor = 'text-emerald-400';
        }

        const progressArray = user.trainerProgressPercent || [];
        const fullyCompleted = progressArray.filter(t => t.progressPercent === 100).length;

        container.innerHTML = `
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                <!-- Основная информация -->
                <div class="md:col-span-2 card p-8">
                    <div class="flex items-center gap-6">
                        <div class="w-20 h-20 bg-slate-700 rounded-2xl flex items-center justify-center text-4xl">
                            👤
                        </div>
                        <div>
                            <h2 class="text-3xl font-bold text-white">${user.firstName} ${user.lastName}</h2>
                            <p class="text-slate-400">${user.email}</p>
                            <p class="${roleColor} font-semibold mt-2">${role}</p>
                        </div>
                    </div>
                </div>

                <!-- Статистика -->
                <div class="card p-8 text-center">
                    <div class="text-5xl font-bold text-cyan-400">${user.totalScore || 0}</div>
                    <p class="text-slate-400 mt-1">Всего баллов</p>
                </div>

                <div class="card p-8">
                    <div class="text-4xl font-bold text-emerald-400">${fullyCompleted}</div>
                    <p class="text-slate-400">Завершено на 100%</p>
                </div>

                <div class="card p-8">
                    <div class="text-4xl font-bold">${progressArray.length}</div>
                    <p class="text-slate-400">Всего тренажёров</p>
                </div>
            </div>

            <!-- Прогресс по тренажёрам -->
            <div class="mt-10">
                <h3 class="text-xl font-bold mb-6 text-white">Прогресс по тренажёрам</h3>
                <div class="space-y-4">
                    ${progressArray.map(trainer => `
                        <div class="card p-6 flex justify-between items-center">
                            <div>
                                <p class="font-medium">${trainer.name}</p>
                            </div>
                            <div class="text-right">
                                <div class="text-xl font-bold ${trainer.progressPercent === 100 ? 'text-emerald-400' : 'text-cyan-400'}">
                                    ${trainer.progressPercent}%
                                </div>
                            </div>
                        </div>
                    `).join('')}
                </div>
            </div>
        `;

    } catch (e) {
        console.error(e);
        container.innerHTML = `<p class="text-red-400">Не удалось загрузить профиль: ${e.message}</p>`;
    }
}

// Автозагрузка
document.addEventListener('DOMContentLoaded', loadProfile);