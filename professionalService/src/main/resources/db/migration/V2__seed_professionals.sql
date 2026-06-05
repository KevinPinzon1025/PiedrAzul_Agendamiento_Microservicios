INSERT INTO professionals (name, email, type_id)
SELECT 'Juan Pérez', 'juan.perez@unicauca.edu.co', id FROM professional_types WHERE name = 'MEDICO'
ON CONFLICT (email) DO NOTHING;

INSERT INTO professionals (name, email, type_id)
SELECT 'María López', 'maria.lopez@unicauca.edu.co', id FROM professional_types WHERE name = 'TERAPISTA'
ON CONFLICT (email) DO NOTHING;

INSERT INTO professionals (name, email, type_id)
SELECT 'Carlos Rodríguez', 'carlos.rodriguez@unicauca.edu.co', id FROM professional_types WHERE name = 'TERAPISTA'
ON CONFLICT (email) DO NOTHING;

INSERT INTO professionals (name, email, type_id)
SELECT 'Ana Martínez', 'ana.martinez@unicauca.edu.co', id FROM professional_types WHERE name = 'MEDICO'
ON CONFLICT (email) DO NOTHING;
