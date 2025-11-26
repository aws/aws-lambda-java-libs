# invoke for handler function

curl -XPOST "http://localhost:9000/2015-03-31/functions/function/invocations" -d '{
    "color_hex": "#FF5733",
    "model": "Tesla Model S"
}'