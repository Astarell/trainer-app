// assets/js/trainers.js

let currentTaskNumber = 1; // для нумерации заданий

// ====================== ЗАГРУЗКА СПИСКА ТРЕНАЖЁРОВ ======================
async function loadTrainers() {
    try {
        const token = localStorage.getItem('token');
        const res = await fetch('http://localhost:8080/api/trainers', {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (!res.ok) throw new Error('Ошибка ' + res.status);

        const trainers = await res.json();
        let html = '';

        trainers.forEach(t => {
            html += `
                <div class="card p-6">
                    <h3 class="text-2xl font-bold text-white">${t.name}</h3>
                    <button onclick="goToTrainer('${t.id}')" 
                            class="mt-6 w-full btn btn-primary py-3">
                        Открыть тренажёр
                    </button>
                </div>`;
        });

        document.getElementById('trainersList').innerHTML = 
            html || '<p class="text-slate-400">Тренажёры не найдены</p>';
    } catch (e) {
        console.error(e);
        alert('Не удалось загрузить тренажёры: ' + e.message);
    }
}

// ====================== ЗАГРУЗКА ДЕТАЛЕЙ ТРЕНАЖЁРА ======================
async function loadTrainerDetail() {
    const urlParams = new URLSearchParams(window.location.search);
    let trainerId = urlParams.get('id') || localStorage.getItem('currentTrainerId');

    if (!trainerId) {
        document.getElementById('trainerName').textContent = 'ID тренажёра не найден';
        return;
    }

    try {
        const token = localStorage.getItem('token');
        const res = await fetch(`http://localhost:8080/api/trainers/${trainerId}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (!res.ok) throw new Error('Ошибка ' + res.status);

        const trainer = await res.json();
        document.getElementById('trainerName').textContent = trainer.name || 'Тренажёр';

        let html = '<h2 class="text-2xl font-bold mb-6 text-white">Задания</h2>';

        if (!trainer.tasks || trainer.tasks.length === 0) {
            html += '<p class="text-slate-400">В этом тренажёре пока нет заданий</p>';
        } else {
            trainer.tasks.forEach((task, index) => {
                const title = task.title || task.question || 'Без названия';
                html += `
                    <div class="card p-6">
                        <h4 class="font-bold text-cyan-400">Задание ${index + 1}</h4>
                        <p class="mt-3 text-slate-300">${title.substring(0, 180)}${title.length > 180 ? '...' : ''}</p>
                        <button onclick="startTask('${trainer.id}', '${task.id}')" 
                                class="mt-6 w-full btn btn-primary py-3">
                            Решать задание
                        </button>
                    </div>`;
            });
        }

        document.getElementById('tasksList').innerHTML = html;
    } catch (e) {
        console.error(e);
        alert('Ошибка загрузки тренажёра: ' + e.message);
    }
}

// ====================== ПЕРЕХОДЫ ======================
function goToTrainer(id) {
    if (!id) return alert('ID тренажёра отсутствует!');
    localStorage.setItem('currentTrainerId', id);
    window.location.href = `trainer-detail.html?id=${id}`;
}

function startTask(trainerId, taskId) {
    if (!trainerId || !taskId) return alert('Ошибка: ID отсутствует!');
    localStorage.setItem('currentTrainerId', trainerId);
    localStorage.setItem('currentTaskId', taskId);
    window.location.href = `task.html?id=${trainerId}&taskId=${taskId}`;
}

// ====================== ЗАГРУЗКА ЗАДАНИЯ ======================
async function loadTask() {
    const urlParams = new URLSearchParams(window.location.search);
    const trainerId = urlParams.get('id');
    const taskId = urlParams.get('taskId') || localStorage.getItem('currentTaskId');

    if (!trainerId || !taskId) return;

    try {
        const token = localStorage.getItem('token');
        const res = await fetch(`http://localhost:8080/api/trainers/${trainerId}/tasks/${taskId}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (!res.ok) throw new Error(await res.text());

        const task = await res.json();
        console.log('🔍 ПОЛНЫЙ ОБЪЕКТ ЗАДАНИЯ:', task);

        const questionText = task.question || 'Текст задания отсутствует';
        const contextText = task.context || null;
        const taskType = task.taskType || 'UNKNOWN';
        const isOpenAnswer = taskType === 'OPEN_ANSWER';

        const attemptStatus = task.attemptStatus || '';
        const userPoints = task.userPoints || 0;
        const attemptsUsed = task.userAttempts || 0;
        const maxAttempts = task.maxAttempts || 3;

        const isCompleted = attemptStatus === 'COMPLETED' || userPoints > 0;
        const isUnderReview = attemptStatus === 'REVIEW';
        const isFailed = attemptStatus === 'FAILED';

        const hasRemainingAttempts = attemptsUsed < maxAttempts;
        const isDisabled = isCompleted || isUnderReview || (!hasRemainingAttempts && isFailed);

        // Парсинг предыдущего ответа
        let lastAnswer = null;
        if (task.answer) {
            try {
                const parsed = typeof task.answer === 'string' ? JSON.parse(task.answer) : task.answer;
                lastAnswer = parsed.answer || parsed.selectedOrdinals || parsed.selectedOrdinal || parsed;
            } catch (e) {
                lastAnswer = task.answer;
            }
        }

        // Тип задания для отображения
        let taskTypeLabel = '';
        if (taskType === 'SINGLE_CHOICE') taskTypeLabel = 'Выберите один правильный вариант';
        else if (taskType === 'MULTIPLE_CHOICE') taskTypeLabel = 'Выберите все правильные варианты';
        else if (taskType === 'OPEN_ANSWER') taskTypeLabel = 'Свободный ответ (проверяется экспертом)';
        else if (taskType === 'ERROR_FINDING') taskTypeLabel = 'Найдите ошибку в коде';

        let html = `
            <h2 class="text-3xl font-bold mb-6 text-cyan-400">Задание</h2>
        `;

        // Для OPEN_ANSWER показываем context перед вопросом
        if (isOpenAnswer && contextText) {
            html += `
                <div class="bg-slate-900 p-6 rounded-2xl mb-6 text-lg leading-relaxed">
                    <div class="text-cyan-400 font-medium mb-3">Контекст:</div>
                    ${escapeHtml(contextText)}
                </div>
            `;
        }

        // Вопрос
        html += `
            <div class="bg-slate-900 p-6 rounded-2xl mb-8 text-lg leading-relaxed">
                <span class="text-cyan-400 font-medium block mb-3">Вопрос:</span>
                ${escapeHtml(questionText)}
            </div>
        `;

        if (taskTypeLabel) {
            html += `<p class="text-cyan-400 text-sm mb-6 font-medium">${taskTypeLabel}</p>`;
        }

        // Статус
        if (isOpenAnswer) {
            let statusHTML = '';
            if (isUnderReview) statusHTML = `<span class="px-5 py-2 bg-amber-600/20 text-amber-400 rounded-2xl text-sm font-medium">⏳ На проверке у эксперта</span>`;
            else if (isCompleted) statusHTML = `<span class="px-5 py-2 bg-emerald-600/20 text-emerald-400 rounded-2xl text-sm font-medium">✅ Принято (+${userPoints} баллов)</span>`;
            else if (isFailed && !hasRemainingAttempts) statusHTML = `<span class="px-5 py-2 bg-red-600/20 text-red-400 rounded-2xl text-sm font-medium">❌ Отклонено экспертом</span>`;

            html += `<div class="flex justify-center mb-6">${statusHTML}</div>`;
        } else {
            let statusHTML = '';
            if (isFailed && !hasRemainingAttempts) {
                statusHTML = `<span class="px-5 py-2 bg-red-600/20 text-red-400 rounded-2xl text-sm font-medium">❌ Попытки закончились</span>`;
            } else if (isCompleted) {
                statusHTML = `<span class="px-5 py-2 bg-emerald-600/20 text-emerald-400 rounded-2xl text-sm font-medium">✅ Выполнено (+${userPoints} баллов)</span>`;
            } else if (isFailed && hasRemainingAttempts) {
                statusHTML = `<span class="px-5 py-2 bg-red-600/20 text-red-400 rounded-2xl text-sm font-medium">❌ Неправильно, осталось попыток: ${maxAttempts - attemptsUsed}</span>`;
            }

            html += `
                <div class="flex items-center justify-between bg-slate-900/70 p-4 rounded-xl mb-6 border border-slate-700">
                    <div class="flex items-center gap-3">
                        <span class="text-slate-400">Попытки:</span>
                        <span class="font-mono font-bold text-xl">${attemptsUsed}/${maxAttempts}</span>
                    </div>
                    ${statusHTML}
                </div>`;
        }

        // Поле ответа
        if (isOpenAnswer || !task.answerChoices) {
            const disabledAttr = isDisabled ? 'readonly disabled' : '';
            html += `<textarea id="openAnswer" rows="10" ${disabledAttr}
                       class="w-full p-5 bg-slate-900 rounded-2xl text-white resize-y min-h-[180px] ${isDisabled ? 'opacity-75' : ''}"
                       placeholder="Введите ваш ответ..."></textarea>`;
        } else {
            html += `<div class="space-y-4" id="choices"></div>`;
        }

        // Кнопка отправки
        let buttonText = 'Отправить ответ';
        if (isUnderReview) buttonText = '⏳ На проверке у эксперта';
        else if (isCompleted) buttonText = '✅ Задание выполнено';
        else if (isFailed && !hasRemainingAttempts) buttonText = '❌ Попытки закончились';

        html += `
            <button onclick="submitAnswer('${trainerId}', '${taskId}', '${taskType}')"
                    id="submitBtn" ${isDisabled ? 'disabled' : ''}
                    class="mt-10 w-full btn btn-primary py-5 text-lg font-semibold ${isDisabled ? 'opacity-50 cursor-not-allowed' : ''}">
                ${buttonText}
            </button>`;

        document.getElementById('taskContainer').innerHTML = html;

        // Рендер вариантов (только если НЕ openAnswer)
        if (!isOpenAnswer && task.answerChoices) {
            let choices = [];
            if (typeof task.answerChoices === 'string') {
                try { choices = JSON.parse(task.answerChoices); } catch(e) {}
            }
            if (choices.length > 0) {
                let chHtml = '';
                const isMultiple = taskType === 'MULTIPLE_CHOICE';
                choices.forEach(ch => {
                    const disabledAttr = isDisabled ? 'disabled' : '';
                    chHtml += `
                        <label class="flex gap-3 p-5 bg-slate-900 rounded-2xl cursor-pointer hover:bg-slate-800 transition-all">
                            <input type="${isMultiple ? 'checkbox' : 'radio'}"
                                   name="ans" value="${ch.ordinal}" ${disabledAttr}
                                   class="mt-1 accent-cyan-400">
                            <span class="text-slate-200">${escapeHtml(ch.choice)}</span>
                        </label>`;
                });
                document.getElementById('choices').innerHTML = chHtml;
            }
        }

        // Предзаполнение ответа
        if (lastAnswer !== null) {
            if (!isOpenAnswer) {
                const prev = Array.isArray(lastAnswer) ? lastAnswer.map(String) : [String(lastAnswer)];
                document.querySelectorAll('input[name="ans"]').forEach(input => {
                    if (prev.includes(input.value)) input.checked = true;
                });
            } else if (document.getElementById('openAnswer')) {
                const text = typeof lastAnswer === 'object' && lastAnswer.answer ? lastAnswer.answer : lastAnswer;
                document.getElementById('openAnswer').value = text;
            }
        }

    } catch (e) {
        console.error('❌ Ошибка loadTask:', e);
        document.getElementById('taskContainer').innerHTML = `<p class="text-red-400">Ошибка: ${e.message}</p>`;
    }
}

// Вспомогательная функция для безопасного экранирования HTML
function escapeHtml(str) {
    if (!str) return '';
    return str
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}

// ====================== ОТПРАВКА ОТВЕТА ======================
async function submitAnswer(trainerId, taskId, taskType) {
    const openAnswerEl = document.getElementById('openAnswer');
    const selectedInputs = document.querySelectorAll('input[name="ans"]:checked');

    let userAnswer = null;

    if (openAnswerEl && openAnswerEl.value.trim()) {
        userAnswer = openAnswerEl.value.trim();
    } else if (selectedInputs.length > 0) {
        const values = Array.from(selectedInputs).map(el => Number(el.value));
        userAnswer = (taskType === 'MULTIPLE_CHOICE') ? values.join(',') : values[0];
    }

    if (!userAnswer) return alert('❌ Введите или выберите ответ!');

    const payload = { userAnswer: userAnswer };

    try {
        const token = localStorage.getItem('token');
        const res = await fetch(`http://localhost:8080/api/trainers/${trainerId}/tasks/${taskId}/submit`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (!res.ok) throw new Error(await res.text());

        const result = await res.json();
        console.log('✅ Ответ от сервера:', result);

        if (taskType === 'OPEN_ANSWER') {
            showToast(result.message || 'Ответ отправлен на проверку эксперту', 'success');
        } else {
            let points = result.userPoints || result.points || 0;
            if (!points && result.message) {
                const match = result.message.match(/(\d+)\s*балл/);
                if (match) points = parseInt(match[1]);
            }

            const isCorrect = points > 0 || result.correct === true || result.attemptStatus === 'COMPLETED';

            showToast(
                isCorrect ? `🎉 Правильно! +${points} баллов` : '😔 Неправильно.',
                isCorrect ? 'success' : 'error'
            );
        }

        setTimeout(() => loadTask(), 900);

    } catch (e) {
        console.error(e);
        alert('❌ ' + e.message);
    }
}

function goBack() {
    const trainerId = localStorage.getItem('currentTrainerId');
    window.location.href = trainerId ? `trainer-detail.html?id=${trainerId}` : 'trainers.html';
}

// Глобальные функции
window.loadTrainers = loadTrainers;
window.loadTrainerDetail = loadTrainerDetail;
window.goToTrainer = goToTrainer;
window.startTask = startTask;
window.loadTask = loadTask;
window.submitAnswer = submitAnswer;
window.goBack = goBack;