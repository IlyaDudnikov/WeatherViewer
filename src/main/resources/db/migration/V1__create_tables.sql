CREATE TABLE IF NOT EXISTS public.locations
(
    id        bigint                                    NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    latitude  decimal(9, 6)                             NOT NULL,
    longitude decimal(9, 6)                             NOT NULL,
    name      varchar(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT locations_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.users
(
    id       bigint                                    NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    login    varchar(255) COLLATE pg_catalog."default" NOT NULL,
    password varchar(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_login_pkey UNIQUE (login)
);

CREATE TABLE IF NOT EXISTS public.user_location
(
    location_id bigint NOT NULL,
    user_id     bigint NOT NULL,
    CONSTRAINT fkjxhhctinrouub93yin6vgvqr4 FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkq7bex2ctcyh1l5eq2jm46hhys FOREIGN KEY (location_id)
        REFERENCES public.locations (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS public.sessions
(
    id         uuid                        NOT NULL,
    user_id    bigint                      NOT NULL,
    expires_at timestamp(6) with time zone NOT NULL,
    CONSTRAINT sessions_pkey PRIMARY KEY (id),
    CONSTRAINT fkruie73rneumyyd1bgo6qw8vjt FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)