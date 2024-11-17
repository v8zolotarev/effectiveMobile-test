CREATE TABLE comments (
id SERIAL PRIMARY KEY,
content TEXT,
author_id BIGINT,
task_id BIGINT,
CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES users(id),
CONSTRAINT fk_comment_task FOREIGN KEY (task_id) REFERENCES tasks(id)
);