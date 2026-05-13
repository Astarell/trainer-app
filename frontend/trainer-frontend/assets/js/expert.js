// assets/js/expert.js

let currentTasks = [];

async function loadForReview() {
    const container = document.getElementById('reviewList');
    container.innerHTML = '<p class="text-slate-400">Загрузка...</p>';

    try {
        const token = localStorage.getItem('token');
        if (!token) return container.innerHTML = `<p class="text-red-400">Войдите как эксперт</p>`;

        const res = await fetch('http://localhost:8080/api/expert/examination', {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (!res.ok) throw new Error(`Ошибка ${res.status}`);

        currentTasks = await res.json();
        console.log('📋 Задания на проверку:', currentTasks);

        if (!currentTasks || currentTasks.length === 0) {
            container.innerHTML = `<p class="text-emerald-400 text-center py-20 text-xl">🎉 Нет заданий на проверку</p>`;
            return;
        }

        let html = `<p class="text-slate-400 mb-6">Заданий на проверку: ${currentTasks.length}</p>`;

        currentTasks.forEach(task => {
            html += `
                <div class="card p-8">
                    <div class="flex justify-between items-start">
                        <div>
                            <h3 class="text-xl font-bold text-white">${task.taskName}</h3>
                            <p class="text-slate-400">${task.trainerName}</p>
                            <p class="text-cyan-400 text-sm">Студент: ${task.studentName}</p>
                        </div>
                        <span class="text-xs text-slate-500">${new Date(task.createdAt).toLocaleString('ru-RU')}</span>
                    </div>

                    <div class="mt-8 flex gap-3">
                        <button onclick="showAnswerModal('${task.attemptId}', '${task.taskName}')" 
                                class="flex-1 btn bg-blue-600 hover:bg-blue-500 py-3">
                            👁 Посмотреть ответ
                        </button>
                        <button onclick="approveSubmission('${task.attemptId}')" 
                                class="flex-1 btn bg-emerald-600 hover:bg-emerald-500 py-3">
                            ✅ Принять
                        </button>
                        <button onclick="rejectSubmission('${task.attemptId}')" 
                                class="flex-1 btn bg-red-600 hover:bg-red-500 py-3">
                            ❌ Отклонить
                        </button>
                    </div>
                </div>`;
        });

        container.innerHTML = html;

    } catch (e) {
        container.innerHTML = `<div class="card p-12 text-center"><p class="text-red-400">${e.message}</p></div>`;
    }
}

// ====================== МОДАЛЬНОЕ ОКНО ======================
function showAnswerModal(attemptId, taskName) {
    let modal = document.getElementById('answerModal');
    if (!modal) {
        modal = document.createElement('div');
        modal.id = 'answerModal';
        modal.className = 'fixed inset-0 bg-black/80 flex items-center justify-center z-50 hidden';
        modal.innerHTML = `
            <div class="bg-slate-900 rounded-3xl w-full max-w-3xl mx-4 max-h-[90vh] flex flex-col">
                <div class="p-6 border-b border-slate-700 flex justify-between items-center">
                    <h3 class="text-xl font-bold text-white" id="modalTitle"></h3>
                    <button onclick="closeModal()" class="text-4xl leading-none text-slate-400 hover:text-white">×</button>
                </div>
                <div class="p-8 overflow-auto flex-1" id="modalAnswer"></div>
                <div class="p-6 border-t border-slate-700 flex justify-end">
                    <button onclick="closeModal()" class="btn px-8 py-3">Закрыть</button>
                </div>
            </div>
        `;
        document.body.appendChild(modal);
    }

    document.getElementById('modalTitle').textContent = taskName;
    document.getElementById('modalAnswer').innerHTML = '<p class="text-slate-400">Загрузка...</p>';
    modal.classList.remove('hidden');

    fetch(`http://localhost:8080/api/expert/examination/${attemptId}`, {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
    })
    .then(r => r.json())
    .then(data => {
        let answerText = 'Ответ не найден';

        // Красиво извлекаем только текст ответа
        if (data.userAnswer) {
            if (typeof data.userAnswer === 'string') {
                try {
                    const parsed = JSON.parse(data.userAnswer);
                    answerText = parsed.answer || data.userAnswer;
                } catch {
                    answerText = data.userAnswer;
                }
            } else if (data.userAnswer.answer) {
                answerText = data.userAnswer.answer;
            }
        } else if (data.answer) {
            answerText = typeof data.answer === 'string' && data.answer.startsWith('{') 
                ? JSON.parse(data.answer).answer || data.answer 
                : data.answer;
        }

        document.getElementById('modalAnswer').innerHTML = `
            <pre class="bg-slate-800 p-6 rounded-2xl text-slate-200 whitespace-pre-wrap leading-relaxed">${answerText}</pre>
        `;
    })
    .catch(() => {
        document.getElementById('modalAnswer').innerHTML = `<p class="text-red-400">Не удалось загрузить ответ</p>`;
    });
}

function closeModal() {
    document.getElementById('answerModal')?.classList.add('hidden');
}

// ====================== ПРИНЯТЬ ======================
async function approveSubmission(attemptId) {
    if (!confirm('Принять ответ студента?')) return;

    try {
        const token = localStorage.getItem('token');
        const res = await fetch(`http://localhost:8080/api/expert/examination/${attemptId}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ isCorrect: true })
        });

        if (!res.ok) throw new Error(await res.text());

        showToast('✅ Ответ принят', 'success');
        loadForReview();

    } catch (e) {
        alert('❌ ' + e.message);
    }
}

// ====================== ОТКЛОНИТЬ ======================
async function rejectSubmission(attemptId) {
    if (!confirm('Отклонить ответ?')) return;

    try {
        const token = localStorage.getItem('token');
        const res = await fetch(`http://localhost:8080/api/expert/examination/${attemptId}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ isCorrect: false })
        });

        if (!res.ok) throw new Error(await res.text());

        showToast('❌ Ответ отклонён', 'error');
        loadForReview();

    } catch (e) {
        alert('❌ ' + e.message);
    }
}

// Глобальные функции
window.loadForReview = loadForReview;
window.showAnswerModal = showAnswerModal;
window.closeModal = closeModal;
window.approveSubmission = approveSubmission;
window.rejectSubmission = rejectSubmission;