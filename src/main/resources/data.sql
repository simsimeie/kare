delete
from RECI_ROUTN_CATG_MP
where RECI_ROUTN_SEQ in (1, 2, 3, 4, 5, 6);

delete
from RECI_ROUTN_MGT
where RECI_ROUTN_SEQ in (1, 2, 3, 4, 5, 6);

delete
from RECI_ROUTN_CATG_MGT
where reci_routn_catg_seq in (1, 2, 3, 4, 5, 6);

insert into RECI_ROUTN_MGT (RECI_ROUTN_SEQ, routn_nm, reci_routn_dsc, ntf_yn, ntf_ti, rpe_cyc_tp_cd, mon_yn, tue_yn,
                            wed_yn, thu_yn, fri_yn, sat_yn, sun_yn, wk_dcn, gol_tp_cd, gol_unit_tp_cd, gol_val, st_dt,
                            en_dt, reg_dtm, chg_dtm)
values (1, '공복러닝', '공복에 러닝하기', 'Y', '05:00:00', 'TIMES', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 7, 3, 'TIMES', 1000,
        '2023-05-01', '2024-12-31', now(), now());

insert into RECI_ROUTN_MGT (RECI_ROUTN_SEQ, routn_nm, reci_routn_dsc, ntf_yn, ntf_ti, rpe_cyc_tp_cd, mon_yn, tue_yn,
                            wed_yn, thu_yn, fri_yn, sat_yn, sun_yn, wk_dcn, gol_tp_cd, gol_unit_tp_cd, gol_val, st_dt,
                            en_dt, reg_dtm, chg_dtm)
values (2, '미라클모닝', '미라클모닝', 'Y', '05:00:00', 'TIMES', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 7, 1, null, null,
        '2023-05-01', '2024-12-31', now(), now());

insert into RECI_ROUTN_MGT (RECI_ROUTN_SEQ, routn_nm, reci_routn_dsc, ntf_yn, ntf_ti, rpe_cyc_tp_cd, mon_yn, tue_yn,
                            wed_yn, thu_yn, fri_yn, sat_yn, sun_yn, wk_dcn, gol_tp_cd, gol_unit_tp_cd, gol_val, st_dt,
                            en_dt, reg_dtm, chg_dtm)
values (3, '쇼핑하기', '쇼핑하기', 'Y', '05:00:00', 'TIMES', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 1, 1, null, null,
        '2023-05-01', '2024-12-31', now(), now());

insert into RECI_ROUTN_MGT (RECI_ROUTN_SEQ, routn_nm, reci_routn_dsc, ntf_yn, ntf_ti, rpe_cyc_tp_cd, mon_yn, tue_yn,
                            wed_yn, thu_yn, fri_yn, sat_yn, sun_yn, wk_dcn, gol_tp_cd, gol_unit_tp_cd, gol_val, st_dt,
                            en_dt, reg_dtm, chg_dtm)
values (4, '게임', '게임', 'Y', '05:00:00', 'TIMES', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 3, 1, null, null,
        '2023-05-01', '2024-12-31', now(), now());

insert into RECI_ROUTN_MGT (RECI_ROUTN_SEQ, routn_nm, reci_routn_dsc, ntf_yn, ntf_ti, rpe_cyc_tp_cd, mon_yn, tue_yn,
                            wed_yn, thu_yn, fri_yn, sat_yn, sun_yn, wk_dcn, gol_tp_cd, gol_unit_tp_cd, gol_val, st_dt,
                            en_dt, reg_dtm, chg_dtm)
values (5, '토이프로젝트', '토이프로젝트', 'Y', '05:00:00', 'TIMES', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 3, 1, null, null,
        '2023-05-01', '2024-12-31', now(), now());

insert into RECI_ROUTN_MGT (RECI_ROUTN_SEQ, routn_nm, reci_routn_dsc, ntf_yn, ntf_ti, rpe_cyc_tp_cd, mon_yn, tue_yn,
                            wed_yn, thu_yn, fri_yn, sat_yn, sun_yn, wk_dcn, gol_tp_cd, gol_unit_tp_cd, gol_val, st_dt,
                            en_dt, reg_dtm, chg_dtm)
values (6, '경제신문읽기', '경제신문읽기', 'Y', '05:00:00', 'TIMES', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 3, 1, null, null,
        '2023-05-01', '2024-12-31', now(), now());

insert into RECI_ROUTN_CATG_MGT(reci_routn_catg_seq, catg_nm, catg_dsc, so_odr, reg_dtm, chg_dtm)
values (1, '운동', '운동', 1, now(), now());
insert into RECI_ROUTN_CATG_MGT(reci_routn_catg_seq, catg_nm, catg_dsc, so_odr, reg_dtm, chg_dtm)
values (2, '생활습관', '생활습관', 2, now(), now());
insert into RECI_ROUTN_CATG_MGT(reci_routn_catg_seq, catg_nm, catg_dsc, so_odr, reg_dtm, chg_dtm)
values (3, '힐링', '힐링', 3, now(), now());
insert into RECI_ROUTN_CATG_MGT(reci_routn_catg_seq, catg_nm, catg_dsc, so_odr, reg_dtm, chg_dtm)
values (4, '취미', '취미', 4, now(), now());
insert into RECI_ROUTN_CATG_MGT(reci_routn_catg_seq, catg_nm, catg_dsc, so_odr, reg_dtm, chg_dtm)
values (5, '성장', '성장', 5, now(), now());
insert into RECI_ROUTN_CATG_MGT(reci_routn_catg_seq, catg_nm, catg_dsc, so_odr, reg_dtm, chg_dtm)
values (6, '경제', '경제', 6, now(), now());



insert into RECI_ROUTN_CATG_MP(reci_routn_catg_seq, reci_routn_seq, so_ord, reg_dtm, chg_dtm)
values (1, 1, 1, now(), now());

insert into RECI_ROUTN_CATG_MP(reci_routn_catg_seq, reci_routn_seq, so_ord, reg_dtm, chg_dtm)
values (2, 2, 1, now(), now());

insert into RECI_ROUTN_CATG_MP(reci_routn_catg_seq, reci_routn_seq, so_ord, reg_dtm, chg_dtm)
values (3, 3, 1, now(), now());

insert into RECI_ROUTN_CATG_MP(reci_routn_catg_seq, reci_routn_seq, so_ord, reg_dtm, chg_dtm)
values (4, 4, 1, now(), now());

insert into RECI_ROUTN_CATG_MP(reci_routn_catg_seq, reci_routn_seq, so_ord, reg_dtm, chg_dtm)
values (5, 5, 1, now(), now());

insert into RECI_ROUTN_CATG_MP(reci_routn_catg_seq, reci_routn_seq, so_ord, reg_dtm, chg_dtm)
values (6, 6, 1, now(), now());
