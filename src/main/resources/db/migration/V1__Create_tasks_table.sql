-- Set timezone FIRST before creating any tables or functions
SET timezone TO 'Asia/Almaty';
ALTER DATABASE taskdb SET timezone TO 'Asia/Almaty';

-- Create sequence for task IDs to match @SequenceGenerator(name = "task_seq", sequenceName = "task_sequence")
CREATE SEQUENCE IF NOT EXISTS task_sequence START WITH 1 INCREMENT BY 1;

-- Create tasks table with proper data types and constraints
CREATE TABLE IF NOT EXISTS tasks (
                                     id BIGINT PRIMARY KEY DEFAULT nextval('task_sequence'),
    title VARCHAR(100) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'Asia/Almaty'),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'Asia/Almaty')
    );

-- Add check constraint for status values
ALTER TABLE tasks DROP CONSTRAINT IF EXISTS chk_task_status;
ALTER TABLE tasks ADD CONSTRAINT chk_task_status
    CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED'));

-- Add check constraint for title length
ALTER TABLE tasks DROP CONSTRAINT IF EXISTS chk_task_title_length;
ALTER TABLE tasks ADD CONSTRAINT chk_task_title_length
    CHECK (char_length(title) <= 100 AND char_length(title) > 0);

-- Create a trigger function to automatically update the updated_at timestamp with Almaty timezone
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    -- Explicitly set timezone to Almaty for the timestamp
    NEW.updated_at = now() AT TIME ZONE 'Asia/Almaty';
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Drop trigger if exists and recreate
DROP TRIGGER IF EXISTS update_tasks_updated_at ON tasks;
CREATE TRIGGER update_tasks_updated_at
    BEFORE UPDATE ON tasks
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Verify timezone settings
SELECT current_setting('timezone') as current_timezone;
SELECT now() as current_time_with_timezone;

-- Test query to verify timestamp behavior
SELECT
    'Asia/Almaty'::text as expected_timezone,
        now() as current_timestamp,
    now() AT TIME ZONE 'Asia/Almaty' as explicit_almaty_time,
    extract(timezone_hour from now()) as timezone_offset_hours;

-- Insert a test record to verify timezone behavior (optional)
-- INSERT INTO tasks (title, description) VALUES ('Timezone Test', 'Testing Almaty timezone');
-- SELECT id, title, created_at, updated_at,
--        extract(timezone_hour from created_at) as created_tz_offset,
--        extract(timezone_hour from updated_at) as updated_tz_offset
-- FROM tasks WHERE title = 'Timezone Test';