wrk.method = "POST"
wrk.headers["Content-Type"] = "application/json"
file = io.open("../resources/xxl.json", "r")
wrk.body = file:read("*a")
