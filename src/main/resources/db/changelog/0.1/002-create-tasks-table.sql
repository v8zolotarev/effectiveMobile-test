CREATE TABLE tasks (
id SERIAL PRIMARY KEY,
title VARCHAR(255),
description TEXT,
status VARCHAR(50),
priority VARCHAR(50),
author_id BIGINT,
assignee_id BIGINT,
CONSTRAINT fk_task_author FOREIGN KEY (author_id) REFERENCES users(id),
CONSTRAINT fk_task_assignee FOREIGN KEY (assignee_id) REFERENCES users(id)
);