SELECT S.address, count(*) as count
FROM stores S join stores_products SA on S.id = SA.store_id
    join products P on SA.product_id = P.id
    join type T on P.type_id = T.id
where T.name = 'X'
group by S.id
LIMIT 1;