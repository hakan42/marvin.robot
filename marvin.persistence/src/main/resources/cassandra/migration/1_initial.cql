CREATE TABLE IF NOT EXISTS environmentdescriptionentry (id uuid, description text, PRIMARY KEY (id));
CREATE TABLE IF NOT EXISTS personentry (id uuid, person_name text, email text, relation text, github_id text, google_id text, PRIMARY KEY (id));
CREATE TABLE IF NOT EXISTS notebookentry (id uuid, created_date timestamp, note_date timestamp, note text, PRIMARY KEY (id));
