-- -- Create function to automatically update the updated_at timestamp
-- CREATE OR REPLACE FUNCTION update_updated_at_column()
-- RETURNS TRIGGER AS $$
-- BEGIN
--     NEW.updated_at = CURRENT_TIMESTAMP;
--     RETURN NEW;
-- END;
-- $$ LANGUAGE plpgsql;
--
-- -- Create trigger to automatically update updated_at on row updates
-- CREATE TRIGGER update_tasks_updated_at
--     BEFORE UPDATE ON tasks
--     FOR EACH ROW
--     EXECUTE FUNCTION update_updated_at_column();