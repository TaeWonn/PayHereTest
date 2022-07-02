INSERT INTO account_book_category (id, name, depth, user_id, parent_id, deletable, created_at, updated_at, deleted_at)
    VALUES
        (1, '미분류', 0, null, null, false, now(), null, null),
        (2, '미분류', 0, null, 1, false, now(), null, null),

        (3, '식비', 0, null, null, false, now(), null, null),
        (4, '주식', 1, null, 3, false, now(), null, null),
        (5, '부식', 1, null, 3, false, now(), null, null),
        (6, '간식', 1, null, 3, false, now(), null, null),
        (7, '외식', 1, null, 3, false, now(), null, null),
        (8, '커피/음료', 1, null, 3, false, now(), null, null),
        (9, '술/유흥', 1, null, 3, false, now(), null, null),
        (10, '기타', 1, null, 3, false, now(), null, null),

        (11, '주거/통신', 0, null, null, false, now(), null, null),
        (12, '관리비', 1, null, 11, false, now(), null, null),
        (13, '공과금', 1, null, 11, false, now(), null, null),
        (14, '이동통신', 1, null, 11, false, now(), null, null),
        (15, '인터넷', 1, null, 11, false, now(), null, null),
        (16, '월세', 1, null, 11, false, now(), null, null),
        (17, '기타', 1, null, 11, false, now(), null, null),

        (18, '생활용품', 0, null, null, false, now(), null, null),
        (19, '가구/가전', 1, null, 18, false, now(), null, null),
        (20, '주방/욕실', 1, null, 18, false, now(), null, null),
        (21, '잡화소모', 1, null, 18, false, now(), null, null),
        (22, '기타', 1, null, 18, false, now(), null, null),

        (23, '의복/미용', 0, null, null, false, now(), null, null),
        (24, '의류비', 1, null, 23, false, now(), null, null),
        (25, '패션잡화', 1, null, 23, false, now(), null, null),
        (26, '헤어/뷰티', 1, null, 23, false, now(), null, null),
        (27, '세탁수선비', 1, null, 23, false, now(), null, null),
        (28, '기타', 1, null, 23, false, now(), null, null),

        (29, '건강/문화', 0, null, null, false, now(), null, null),
        (30, '운동/레저', 1, null, 29, false, now(), null, null),
        (31, '문화생활', 1, null, 29, false, now(), null, null),
        (32, '여행', 1, null, 29, false, now(), null, null),
        (33, '병원비', 1, null, 29, false, now(), null, null),
        (34, '보장성보험', 1, null, 29, false, now(), null, null),
        (35, '기타', 1, null, 29, false, now(), null, null),

        (36, '교육/육아', 0, null, null, false, now(), null, null),
        (37, '등록금', 1, null, 36, false, now(), null, null),
        (38, '학원/교재비', 1, null, 36, false, now(), null, null),
        (39, '육아용품', 1, null, 36, false, now(), null, null),
        (40, '기타', 1, null, 36, false, now(), null, null),

        (41, '교통/차량', 0, null, null, false, now(), null, null),
        (42, '대중교통비', 1, null, 41, false, now(), null, null),
        (43, '주유비', 1, null, 41, false, now(), null, null),
        (44, '자동차보험', 1, null, 41, false, now(), null, null),
        (45, '기타', 1, null, 41, false, now(), null, null),

        (46, '경조사/회비', 0, null, null, false, now(), null, null),
        (47, '경조사비', 1, null, 46, false, now(), null, null),
        (48, '모임회비', 1, null, 46, false, now(), null, null),
        (49, '데이트', 1, null, 46, false, now(), null, null),
        (50, '선물', 1, null, 46, false, now(), null, null),
        (51, '기타', 1, null, 46, false, now(), null, null),

        (52, '세금/이자', 0, null, null, false, now(), null, null),
        (53, '세금', 1, null, 52, false, now(), null, null),
        (54, '대출이자', 1, null, 52, false, now(), null, null),
        (55, '기타', 1, null, 52, false, now(), null, null),

        (56, '용돈/기타', 0, null, null, false, now(), null, null),
        (57, '용돈', 1, null, 56, false, now(), null, null),
        (58, '기타', 1, null, 56, false, now(), null, null),

        (59, '저축/보험', 0, null, null, false, now(), null, null),
        (60, '예금', 1, null, 59, false, now(), null, null),
        (61, '적금', 1, null, 59, false, now(), null, null),
        (62, '펀트', 1, null, 59, false, now(), null, null),
        (63, '보험', 1, null, 59, false, now(), null, null),
        (64, '투자', 1, null, 59, false, now(), null, null),
        (65, '기타', 1, null, 59, false, now(), null, null),

        (66, '이체/대체', 0, null, null, false, now(), null, null),

        (67, '카드대금', 0, null, null, false, now(), null, null)
   ;