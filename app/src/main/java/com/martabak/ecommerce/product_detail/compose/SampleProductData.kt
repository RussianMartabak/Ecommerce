package com.martabak.ecommerce.product_detail.compose

import com.martabak.ecommerce.network.data.product_detail.Data
import com.martabak.ecommerce.network.data.product_detail.ProductVariant
import com.martabak.ecommerce.network.data.product_detail.ReviewData

var sampleProductData = Data(
    productId = "17b4714d-527a-4be2-84e2-e4c37c2b3292",
    productName = "description ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray [AMD Ryzen™ 7 6800H / NVIDIA® GeForce RTX™ 3060 / 8G*2 / 512GB / 17.3inch / WIN11 / OHS]\n\nCPU : AMD Ryzen™ 7 6800H Mobile Processor (8-core/16-thread, 20MB cache, up to 4.7 GHz max boost)\nGPU : NVIDIA® GeForce RTX™ 3060 Laptop GPU\nGraphics Memory : 6GB GDDR6\nDiscrete/Optimus : MUX Switch + Optimus\nTGP ROG Boost : 1752MHz* at 140W (1702MHz Boost Clock+50MHz OC, 115W+25W Dynamic Boost)\nPanel : 17.3-inch FHD (1920 x 1080) 16:9 360Hz IPS-level 300nits sRGB % 100.00%",
    productPrice = 24499000,
    image = listOf(
        "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/4/6/0a49c399-cf6b-47f5-91c9-8cbd0b86462d.jpg",
        "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/3/25/0cc3d06c-b09d-4294-8c3f-1c37e60631a6.jpg",
        "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/3/25/33a06657-9f88-4108-8676-7adafaa94921.jpg"
    ),
    brand = "Asus",
    description = "ASUS ROG Strix ",
    store = "AsusStore",
    sale = 12,
    stock = 2,
    totalRating = 7,
    totalReview = 5,
    totalSatisfaction = 100,
    productRating = 5.0,
    productVariant = listOf(
        ProductVariant(variantName = "RAM 16GB", variantPrice = 0),
        ProductVariant(variantName = "RAM 32GB", variantPrice = 100000)
    )
)

val sampleReviewData = listOf(
    ReviewData(
        userName = "John",
        userImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQM4VpzpVw8mR2j9_gDajEthwY3KCOWJ1tOhcv47-H9o1a-s9GRPxdb_6G9YZdGfv0HIg&usqp=CAU",
        userRating = 4,
        userReview = "Cheap price but not powerful enough to play Deep Rock Galactic"
    ),
    ReviewData(
        userName = "Doe",
        userImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTR3Z6PN8QNVhH0e7rEINu_XJS0qHIFpDT3nwF5WSkcYmr3znhY7LOTkc8puJ68Bts-TMc&usqp=CAU",
        userRating = 5,
        userReview = "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."

    )
)
