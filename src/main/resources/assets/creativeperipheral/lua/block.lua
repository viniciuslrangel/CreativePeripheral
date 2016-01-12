block = {}
local p = peripheral.find("Creative")

function block.getAll()
    p.getAllBlocksName();
end

function block.get(dimension, coords)
    local obj = {}

    local x = coords.x
    local y = coords.y
    local z = coords.z

    function obj:isAir()
        local a, b, c = p.getBlock(dimension, x, y, z);
        return a == "air";
    end
    function obj:getWorld()
        return dimension;
    end
    function obj:setWorld(id)
        dimension = id
    end
    function obj:getCoords()
        return vector.new(x, y ,z)
    end
    function obj:setCoords(vec)
        x = vec.x
        y = vec.y
        z = vec.z
    end
    function obj:getName()
        return p.getBlock(dimension, x, y, z)
    end
    function obj:place(name)
        p.setBlock(dimension, x, y, z, name)
    end

    return obj
end