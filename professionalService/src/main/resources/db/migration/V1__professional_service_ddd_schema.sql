CREATE TABLE IF NOT EXISTS professional_types (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS professionals (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(200) NOT NULL UNIQUE,
    type_id BIGINT NOT NULL,
    CONSTRAINT fk_professional_type
        FOREIGN KEY (type_id) REFERENCES professional_types(id)
);

CREATE TABLE IF NOT EXISTS professional_working_days (
    id BIGSERIAL PRIMARY KEY,
    professional_id BIGINT NOT NULL,
    day_of_week VARCHAR(20) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    appointment_duration_minutes INTEGER NOT NULL,
    CONSTRAINT fk_working_day_professional
        FOREIGN KEY (professional_id) REFERENCES professionals(id) ON DELETE CASCADE,
    CONSTRAINT ck_working_day_time_range CHECK (start_time < end_time),
    CONSTRAINT ck_working_day_duration CHECK (appointment_duration_minutes > 0)
);

CREATE INDEX IF NOT EXISTS idx_working_days_professional_day
    ON professional_working_days(professional_id, day_of_week);

CREATE TABLE IF NOT EXISTS scheduling_configurations (
    id BIGINT PRIMARY KEY,
    autonomous_scheduling_window_weeks INTEGER NOT NULL,
    CONSTRAINT ck_scheduling_window_weeks CHECK (autonomous_scheduling_window_weeks > 0)
);

CREATE TABLE IF NOT EXISTS holidays (
    holiday_date DATE PRIMARY KEY,
    name VARCHAR(150) NOT NULL
);

INSERT INTO professional_types (name) VALUES ('MEDICO') ON CONFLICT (name) DO NOTHING;
INSERT INTO professional_types (name) VALUES ('TERAPISTA') ON CONFLICT (name) DO NOTHING;

INSERT INTO scheduling_configurations (id, autonomous_scheduling_window_weeks)
VALUES (1, 4)
ON CONFLICT (id) DO NOTHING;
