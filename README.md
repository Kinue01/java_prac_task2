Так как приложение работает через XServer, для запуска необходимо использовать следующую команду: docker compose run --rm -e DISPLAY=host.docker.internal:0.0 -v /tmp/.X11-unix:/tmp/.X11-unix jfx с запущенным приложением XServer (тестировалось на VcXsrv)
