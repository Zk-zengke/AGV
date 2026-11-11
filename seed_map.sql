INSERT INTO agv_system.agv_map
(map_code, map_name, width, height, cell_size, description, obstacles, stations, version, is_current, parent_id)
VALUES
('map_seed_001', '默认车间地图', 20, 20, 1, '车间AGV仿真默认地图，含中央隔断墙与四个工位',
'[{"x":10,"y":2},{"x":10,"y":3},{"x":10,"y":4},{"x":10,"y":5},{"x":10,"y":6},{"x":10,"y":7},{"x":10,"y":8},{"x":10,"y":9},{"x":10,"y":11},{"x":10,"y":12},{"x":10,"y":13},{"x":10,"y":14},{"x":10,"y":15},{"x":10,"y":16},{"x":10,"y":17},{"x":5,"y":12},{"x":6,"y":12},{"x":14,"y":5},{"x":13,"y":6}]',
'[{"x":18,"y":18,"id":"charge01","type":"charge"},{"x":2,"y":2,"id":"station01","type":"station"},{"x":17,"y":2,"id":"station02","type":"station"},{"x":2,"y":17,"id":"station03","type":"station"}]',
1, 1, 0);
