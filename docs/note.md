CREATE TABLE Account_Club (
account_club_id INT AUTO_INCREMENT PRIMARY KEY,
account_id INT NOT NULL,
club_id INT NOT NULL,
is_deleted BOOLEAN DEFAULT FALSE,

    FOREIGN KEY (account_id) REFERENCES Account(account_id),
    FOREIGN KEY (club_id) REFERENCES Club(club_id),

    UNIQUE (account_id, club_id)
);


(21/12) Thêm khóa ngoại club_id cho bảng court
ALTER TABLE court
ADD COLUMN club_id INT NOT NULL,
ADD CONSTRAINT fk_court_club
FOREIGN KEY (club_id)
REFERENCES club(club_id)
ON DELETE RESTRICT
ON UPDATE CASCADE;