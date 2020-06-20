wrk.method = "POST"
wrk.headers["Content-Type"] = "application/json"
file = io.open("../resources/xxs.json", "r")
wrk.body = file:read("*a")
