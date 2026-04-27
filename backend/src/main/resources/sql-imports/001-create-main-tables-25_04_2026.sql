DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'user_roles') THEN
        CREATE TYPE user_roles AS ENUM ('APP_USER', 'APP_EXPERT', 'APP_ADMIN');
    END IF;
END$$;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'task_types') THEN
        CREATE TYPE task_types AS ENUM ('SINGLE_CHOICE', 'MULTIPLE_CHOICE', 'ERROR_FINDING', 'OPEN_ANSWER', 'SQL_QUERY');
    END IF;
END$$;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'task_attempts_status') THEN
        CREATE TYPE task_attempts_status AS ENUM ('REVIEW', 'COMPLETED', 'FAILED');
    END IF;
END$$;

CREATE TABLE IF NOT EXISTS users (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	email VARCHAR(255) UNIQUE NOT NULL,
	app_role user_roles NOT NULL,
	created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS tasks (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	task_type task_types NOT NULL,
	config JSONB NOT NULL,
	created_by UUID NOT NULL,
	created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS trainers (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(255) NOT NULL,
	created_by UUID NOT NULL,
	created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS tasks_trainers (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	task_id UUID NOT NULL,
	trainer_id UUID NOT NULL,
	FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE,
	FOREIGN KEY (trainer_id) REFERENCES trainers (id) ON DELETE CASCADE,
	UNIQUE(task_id, trainer_id)
);

CREATE TABLE IF NOT EXISTS task_attempts (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	task_id UUID NOT NULL,
	user_id UUID NOT NULL,
	user_answer JSONB NOT NULL,
	points DECIMAL NOT NULL,
	status task_attempts_status NOT NULL,
	created_at TIMESTAMPTZ DEFAULT NOW(),
	FOREIGN KEY (task_id) REFERENCES tasks_trainers (id) ON DELETE CASCADE,
	FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
